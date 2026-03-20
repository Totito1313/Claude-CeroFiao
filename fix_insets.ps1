$files = Get-ChildItem -Path "c:\Users\Schwa\OneDrive\Desktop\SchwarckDev\CeroFiao\feature" -Filter "*Screen.kt" -Recurse

foreach ($file in $files) {
    $content = Get-Content $file.FullName -Raw

    $needsChange = $false

    # Import logic
    if ($content -match '\.background\(\s*t\.bg\s*\)') {
        if ($content -notmatch 'import androidx\.compose\.foundation\.layout\.statusBarsPadding') {
            $content = $content -replace '(?m)^(import androidx\.compose\.[^\r\n]+)$', "`$1`r`nimport androidx.compose.foundation.layout.statusBarsPadding"
            # It will match all of them, so we just replace the first one. Wait, replace with Regex will replace ALL.
            # Let's do a trick: find the first import line and insert after it.
            $lines = $content -split "`r?`n"
            $newLines = @()
            $importAdded = $false
            foreach ($line in $lines) {
                $newLines += $line
                if (-not $importAdded -and $line -match '^import androidx\.compose\.') {
                    $newLines += "import androidx.compose.foundation.layout.statusBarsPadding"
                    $importAdded = $true
                }
            }
            $content = $newLines -join "`r`n"
            $needsChange = $true
        }

        # Update modifier
        if ($content -match '\.background\(\s*t\.bg\s*\)(?!\.statusBarsPadding\(\))') {
            $content = $content -replace '\.background\(\s*t\.bg\s*\)(?!\.statusBarsPadding\(\))', '.background(t.bg).statusBarsPadding()'
            $needsChange = $true
        }
    }

    if ($needsChange) {
        Set-Content -Path $file.FullName -Value $content
        Write-Host "Updated $($file.Name)"
    }
}
