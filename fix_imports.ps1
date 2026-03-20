$files = Get-ChildItem -Path "c:\Users\Schwa\OneDrive\Desktop\SchwarckDev\CeroFiao\feature" -Filter "*Screen.kt" -Recurse

foreach ($file in $files) {
    if ($file.Name -match "Screen.kt") {
        $lines = Get-Content $file.FullName
        $cleaned = $lines | Where-Object { $_ -notmatch 'import androidx\.compose\.foundation\.layout\.statusBarsPadding' }
        
        $newContent = @()
        $newContent += $cleaned[0]
        $newContent += "import androidx.compose.foundation.layout.statusBarsPadding"
        for ($i = 1; $i -lt $cleaned.Count; $i++) {
            $newContent += $cleaned[$i]
        }
        
        Set-Content -Path $file.FullName -Value ($newContent -join "`r`n")
        Write-Host "Cleaned $($file.Name)"
    }
}
