package com.schwarckdev.cerofiao.feature.dashboard

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.composables.icons.lucide.AlignCenter
import com.composables.icons.lucide.AlignJustify
import com.composables.icons.lucide.AlignLeft
import com.composables.icons.lucide.AlignRight
import com.composables.icons.lucide.Bell
import com.composables.icons.lucide.ChevronDown
import com.composables.icons.lucide.CreditCard
import com.composables.icons.lucide.GitFork
import com.composables.icons.lucide.Image
import com.composables.icons.lucide.Lucide
import com.composables.icons.lucide.MoreHorizontal
import com.composables.icons.lucide.Settings
import com.composables.icons.lucide.Star
import com.composables.icons.lucide.ThumbsUp
import com.composables.icons.lucide.User
import com.composables.icons.lucide.Video
import com.schwarckdev.cerofiao.core.designsystem.components.buttons.ButtonGroupItem
import com.schwarckdev.cerofiao.core.designsystem.components.buttons.ButtonSize
import com.schwarckdev.cerofiao.core.designsystem.components.buttons.ButtonVariant
import com.schwarckdev.cerofiao.core.designsystem.components.buttons.CeroFiaoButton
import com.schwarckdev.cerofiao.core.designsystem.components.buttons.CeroFiaoButtonGroup
import com.schwarckdev.cerofiao.core.designsystem.components.buttons.CeroFiaoCloseButton
import com.schwarckdev.cerofiao.core.designsystem.components.buttons.CeroFiaoLinkButton
import com.schwarckdev.cerofiao.core.designsystem.components.cards.CardVariant
import com.schwarckdev.cerofiao.core.designsystem.components.cards.CeroFiaoCard
import com.schwarckdev.cerofiao.core.designsystem.components.cards.CeroFiaoCardBody
import com.schwarckdev.cerofiao.core.designsystem.components.cards.CeroFiaoCardDescription
import com.schwarckdev.cerofiao.core.designsystem.components.cards.CeroFiaoCardFooter
import com.schwarckdev.cerofiao.core.designsystem.components.cards.CeroFiaoCardHeader
import com.schwarckdev.cerofiao.core.designsystem.components.cards.CeroFiaoCardTitle
import com.schwarckdev.cerofiao.core.designsystem.components.collections.CeroFiaoTagGroup
import com.schwarckdev.cerofiao.core.designsystem.components.data_display.CeroFiaoChip
import com.schwarckdev.cerofiao.core.designsystem.components.data_display.ChipVariant
import com.schwarckdev.cerofiao.core.designsystem.components.feedback.AlertColor
import com.schwarckdev.cerofiao.core.designsystem.components.feedback.AlertVariant
import com.schwarckdev.cerofiao.core.designsystem.components.feedback.CeroFiaoAlert
import com.schwarckdev.cerofiao.core.designsystem.components.feedback.CeroFiaoSkeleton
import com.schwarckdev.cerofiao.core.designsystem.components.feedback.CeroFiaoSkeletonGroup
import com.schwarckdev.cerofiao.core.designsystem.components.feedback.CeroFiaoSpinner
import com.schwarckdev.cerofiao.core.designsystem.components.feedback.SpinnerSize
import com.schwarckdev.cerofiao.core.designsystem.components.forms.CeroFiaoCheckbox
import com.schwarckdev.cerofiao.core.designsystem.components.forms.CeroFiaoControlField
import com.schwarckdev.cerofiao.core.designsystem.components.forms.CeroFiaoInputOTP
import com.schwarckdev.cerofiao.core.designsystem.components.forms.CeroFiaoRadioGroup
import com.schwarckdev.cerofiao.core.designsystem.components.forms.AutocompleteItem
import com.schwarckdev.cerofiao.core.designsystem.components.forms.CeroFiaoAutocomplete
import com.schwarckdev.cerofiao.core.designsystem.components.forms.CeroFiaoSearchField
import com.schwarckdev.cerofiao.core.designsystem.components.forms.CeroFiaoSelect
import com.schwarckdev.cerofiao.core.designsystem.components.forms.CeroFiaoTagLabel
import com.schwarckdev.cerofiao.core.designsystem.components.forms.InputColor
import com.schwarckdev.cerofiao.core.designsystem.components.forms.InputDisplayVariant
import com.schwarckdev.cerofiao.core.designsystem.components.forms.InputLabelPlacement
import com.schwarckdev.cerofiao.core.designsystem.components.forms.InputSize
import com.schwarckdev.cerofiao.core.designsystem.components.forms.LabelSize
import com.schwarckdev.cerofiao.core.designsystem.components.forms.TagLabelColor
import com.schwarckdev.cerofiao.core.designsystem.components.forms.TagLabelVariant
import com.schwarckdev.cerofiao.core.designsystem.components.controls.CeroFiaoSlider
import com.schwarckdev.cerofiao.core.designsystem.components.controls.CeroFiaoSwitch
import com.schwarckdev.cerofiao.core.designsystem.components.forms.CeroFiaoTextArea
import com.schwarckdev.cerofiao.core.designsystem.components.forms.CeroFiaoTextField
import com.schwarckdev.cerofiao.core.designsystem.components.layout.CeroFiaoSeparator
import com.schwarckdev.cerofiao.core.designsystem.components.layout.CeroFiaoSurface
import com.schwarckdev.cerofiao.core.designsystem.components.media.AvatarSize
import com.schwarckdev.cerofiao.core.designsystem.components.media.CeroFiaoAvatar
import com.schwarckdev.cerofiao.core.designsystem.components.navigation.CeroFiaoAccordion
import com.schwarckdev.cerofiao.core.designsystem.components.navigation.CeroFiaoListGroup
import com.schwarckdev.cerofiao.core.designsystem.components.navigation.CeroFiaoTabs
import com.schwarckdev.cerofiao.core.designsystem.components.overlays.CeroFiaoBottomSheet
import com.schwarckdev.cerofiao.core.designsystem.components.overlays.CeroFiaoDialog
import com.schwarckdev.cerofiao.core.designsystem.components.overlays.CeroFiaoToastHost
import com.schwarckdev.cerofiao.core.designsystem.components.overlays.ToastData
import com.schwarckdev.cerofiao.core.designsystem.components.overlays.ToastVariant
import com.schwarckdev.cerofiao.core.designsystem.components.overlays.rememberCeroFiaoToastHostState
import com.schwarckdev.cerofiao.core.designsystem.theme.LocalCeroFiaoColors
import kotlinx.coroutines.launch

@OptIn(ExperimentalLayoutApi::class, ExperimentalMaterial3Api::class)
@Composable
fun ComponentOverviewSection() {
    val colors = LocalCeroFiaoColors.current

    CeroFiaoAccordion(allowMultipleOpen = true, modifier = Modifier.fillMaxWidth()) {

        // ─── Botones ───────────────────────────────────────────────────────────
        Item(title = "Botones", subtitle = "CeroFiaoButton · CloseButton · LinkButton · ButtonGroup") {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // CeroFiaoButton variants
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    CeroFiaoButton(onClick = {}, text = "Primary")
                    CeroFiaoButton(onClick = {}, text = "Secondary", variant = ButtonVariant.Secondary)
                    CeroFiaoButton(onClick = {}, text = "Outline", variant = ButtonVariant.Outline)
                    CeroFiaoButton(onClick = {}, text = "Ghost", variant = ButtonVariant.Ghost)
                    CeroFiaoButton(onClick = {}, text = "Tertiary", variant = ButtonVariant.Tertiary)
                    CeroFiaoButton(onClick = {}, text = "Danger", variant = ButtonVariant.Danger)
                    CeroFiaoButton(onClick = {}, text = "DangerSoft", variant = ButtonVariant.DangerSoft)
                }
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    CeroFiaoCloseButton(onClick = {})
                    CeroFiaoLinkButton(text = "Ver más →", onClick = {})
                    CeroFiaoButton(onClick = {}, icon = Lucide.Bell, variant = ButtonVariant.Ghost)
                    CeroFiaoButton(onClick = {}, text = "Cargando", isLoading = true)
                }

                // ─── CeroFiaoButtonGroup ─────────────────────────────────────
                // Row 1: Solid primary split, flat with badge, flat plain
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    // Solid — "Merge pull request | ▼"
                    CeroFiaoButtonGroup(
                        variant = ButtonVariant.Primary,
                        size = ButtonSize.Small,
                        items = listOf(
                            ButtonGroupItem(key = "merge", text = "Merge pull request", onClick = {}),
                            ButtonGroupItem(key = "dd", icon = Lucide.ChevronDown, onClick = {}),
                        ),
                    )
                    // Flat with badge — "⑂ Fork | 4 | ▼"
                    CeroFiaoButtonGroup(
                        variant = ButtonVariant.Tertiary,
                        size = ButtonSize.Small,
                        items = listOf(
                            ButtonGroupItem(key = "fork", icon = Lucide.GitFork, text = "Fork", badge = "4", onClick = {}),
                            ButtonGroupItem(key = "dd", icon = Lucide.ChevronDown, onClick = {}),
                        ),
                    )
                    // Active item — "👍 Like | 43"
                    CeroFiaoButtonGroup(
                        variant = ButtonVariant.Tertiary,
                        size = ButtonSize.Small,
                        items = listOf(
                            ButtonGroupItem(key = "like", icon = Lucide.ThumbsUp, text = "Like", isActive = true, onClick = {}),
                            ButtonGroupItem(key = "count", badge = "43", isActive = true, onClick = {}),
                        ),
                    )
                    // Flat with count — "☆ Star | 104"
                    CeroFiaoButtonGroup(
                        variant = ButtonVariant.Tertiary,
                        size = ButtonSize.Small,
                        items = listOf(
                            ButtonGroupItem(key = "star", icon = Lucide.Star, text = "Star", onClick = {}),
                            ButtonGroupItem(key = "count", text = "104", onClick = {}),
                        ),
                    )
                }

                // Row 2: Navigation, media, alignment, icon-only
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    // "‹ | Previous"
                    CeroFiaoButtonGroup(
                        variant = ButtonVariant.Tertiary,
                        size = ButtonSize.Small,
                        items = listOf(
                            ButtonGroupItem(key = "prev_icon", icon = Lucide.ChevronDown, onClick = {}),
                            ButtonGroupItem(key = "prev", text = "Previous", onClick = {}),
                        ),
                    )
                    // "Next | ›"
                    CeroFiaoButtonGroup(
                        variant = ButtonVariant.Tertiary,
                        size = ButtonSize.Small,
                        items = listOf(
                            ButtonGroupItem(key = "next", text = "Next", onClick = {}),
                            ButtonGroupItem(key = "next_icon", icon = Lucide.ChevronDown, onClick = {}),
                        ),
                    )
                    // "🖼 Photos | 📹 Videos | ···"
                    CeroFiaoButtonGroup(
                        variant = ButtonVariant.Tertiary,
                        size = ButtonSize.Small,
                        items = listOf(
                            ButtonGroupItem(key = "photos", icon = Lucide.Image, text = "Photos", onClick = {}),
                            ButtonGroupItem(key = "videos", icon = Lucide.Video, text = "Videos", onClick = {}),
                            ButtonGroupItem(key = "more", icon = Lucide.MoreHorizontal, onClick = {}),
                        ),
                    )
                    // "Left | Center | Right"
                    CeroFiaoButtonGroup(
                        variant = ButtonVariant.Tertiary,
                        size = ButtonSize.Small,
                        items = listOf(
                            ButtonGroupItem(key = "left", text = "Left", onClick = {}),
                            ButtonGroupItem(key = "center", text = "Center", onClick = {}),
                            ButtonGroupItem(key = "right", text = "Right", onClick = {}),
                        ),
                    )
                    // Icon-only group — alignment icons
                    CeroFiaoButtonGroup(
                        variant = ButtonVariant.Tertiary,
                        size = ButtonSize.Small,
                        items = listOf(
                            ButtonGroupItem(key = "al", icon = Lucide.AlignLeft, onClick = {}),
                            ButtonGroupItem(key = "ac", icon = Lucide.AlignCenter, onClick = {}),
                            ButtonGroupItem(key = "ar", icon = Lucide.AlignRight, onClick = {}),
                            ButtonGroupItem(key = "aj", icon = Lucide.AlignJustify, onClick = {}),
                        ),
                    )
                }

                // Row 3: Bordered + Ghost variants
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    CeroFiaoButtonGroup(
                        variant = ButtonVariant.Secondary,
                        size = ButtonSize.Small,
                        items = listOf(
                            ButtonGroupItem(key = "a", text = "Bordered A", onClick = {}),
                            ButtonGroupItem(key = "b", text = "B", onClick = {}),
                        ),
                    )
                    CeroFiaoButtonGroup(
                        variant = ButtonVariant.Ghost,
                        size = ButtonSize.Small,
                        items = listOf(
                            ButtonGroupItem(key = "a", text = "Ghost A", onClick = {}),
                            ButtonGroupItem(key = "b", text = "B", onClick = {}),
                        ),
                    )
                    CeroFiaoButtonGroup(
                        variant = ButtonVariant.Danger,
                        size = ButtonSize.Small,
                        items = listOf(
                            ButtonGroupItem(key = "a", text = "Danger", onClick = {}),
                            ButtonGroupItem(key = "b", icon = Lucide.ChevronDown, onClick = {}),
                        ),
                    )
                    CeroFiaoButtonGroup(
                        variant = ButtonVariant.Tertiary,
                        size = ButtonSize.Small,
                        isDisabled = true,
                        items = listOf(
                            ButtonGroupItem(key = "a", text = "Disabled", onClick = {}),
                            ButtonGroupItem(key = "b", text = "Group", onClick = {}),
                        ),
                    )
                }
            }
        }

        // ─── Feedback ─────────────────────────────────────────────────────────
        Item(title = "Feedback", subtitle = "Alert · Spinner · Skeleton") {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                CeroFiaoAlert(title = "Información del sistema", color = AlertColor.Default)
                CeroFiaoAlert(title = "Operación exitosa", color = AlertColor.Success)
                CeroFiaoAlert(title = "Revisa antes de continuar", color = AlertColor.Warning)
                CeroFiaoAlert(
                    title = "Error crítico",
                    description = "No se pudo completar la operación.",
                    color = AlertColor.Danger,
                    onDismiss = {}
                )
                CeroFiaoAlert(title = "Sólido primario", color = AlertColor.Primary, variant = AlertVariant.Solid)
                CeroFiaoAlert(title = "Con borde", color = AlertColor.Success, variant = AlertVariant.Bordered)
                CeroFiaoAlert(title = "Desvanecido", color = AlertColor.Warning, variant = AlertVariant.Faded)

                CeroFiaoSeparator(modifier = Modifier.padding(vertical = 4.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(20.dp)
                ) {
                    CeroFiaoSpinner(size = SpinnerSize.Small)
                    CeroFiaoSpinner(size = SpinnerSize.Medium)
                    CeroFiaoSpinner(size = SpinnerSize.Large)
                    Text("Spinners S / M / L", fontSize = 12.sp, color = colors.TextSecondary)
                }

                CeroFiaoSeparator(modifier = Modifier.padding(vertical = 4.dp))

                Text("Skeleton:", fontSize = 12.sp, color = colors.TextSecondary)
                CeroFiaoSkeletonGroup(count = 3) {
                    CeroFiaoSkeleton(
                        modifier = Modifier
                            .fillMaxWidth(if (it == 1) 0.7f else 1f)
                            .height(16.dp)
                    )
                }
            }
        }

        // ─── Formularios ──────────────────────────────────────────────────────
        Item(title = "Formularios", subtitle = "Input · Search · Autocomplete · más") {
            var text by remember { mutableStateOf("") }
            var textBordered by remember { mutableStateOf("") }
            var textUnderlined by remember { mutableStateOf("") }
            var textInside by remember { mutableStateOf("") }
            var search by remember { mutableStateOf("") }
            var autocompleteKey by remember { mutableStateOf<String?>(null) }
            var area by remember { mutableStateOf("") }
            var checked by remember { mutableStateOf(false) }
            var switchOn by remember { mutableStateOf(true) }
            var slider by remember { mutableStateOf(0.4f) }
            var selected by remember { mutableStateOf<String?>(null) }
            var radio by remember { mutableStateOf("BCV") }
            var otp by remember { mutableStateOf("") }

            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                // Input variants
                CeroFiaoTextField(
                    value = text,
                    onValueChange = { text = it },
                    label = "Flat (default)",
                    placeholder = "Ingresa texto...",
                )
                CeroFiaoTextField(
                    value = textBordered,
                    onValueChange = { textBordered = it },
                    label = "Bordered",
                    placeholder = "Con borde en foco",
                    displayVariant = InputDisplayVariant.Bordered,
                    color = InputColor.Primary,
                )
                CeroFiaoTextField(
                    value = textUnderlined,
                    onValueChange = { textUnderlined = it },
                    label = "Underlined",
                    placeholder = "Solo línea inferior",
                    displayVariant = InputDisplayVariant.Underlined,
                    color = InputColor.Primary,
                )
                CeroFiaoTextField(
                    value = textInside,
                    onValueChange = { textInside = it },
                    label = "Etiqueta flotante",
                    labelPlacement = InputLabelPlacement.Inside,
                    displayVariant = InputDisplayVariant.Flat,
                )
                // Search — Sm / Md / Lg sizes
                CeroFiaoSearchField(
                    query = search,
                    onQueryChange = { search = it },
                    placeholder = "Buscar (Md)...",
                )
                CeroFiaoSearchField(
                    query = search,
                    onQueryChange = { search = it },
                    size = InputSize.Sm,
                    placeholder = "Buscar (Sm)...",
                )
                CeroFiaoSearchField(
                    query = search,
                    onQueryChange = { search = it },
                    size = InputSize.Lg,
                    placeholder = "Buscar (Lg)...",
                )
                // Autocomplete
                CeroFiaoAutocomplete(
                    items = listOf("USD", "VES", "USDT", "EUR", "BTC", "ETH").map {
                        AutocompleteItem(key = it, label = it, value = it)
                    },
                    selectedKey = autocompleteKey,
                    onSelectionChange = { autocompleteKey = it.key },
                    label = "Moneda",
                    placeholder = "Buscar moneda...",
                )
                CeroFiaoTextArea(
                    value = area,
                    onValueChange = { area = it },
                    label = "Descripción",
                    placeholder = "Escribe una descripción...",
                )
                CeroFiaoCheckbox(
                    checked = checked,
                    onCheckedChange = { checked = it },
                    label = "Acepto los términos y condiciones",
                )
                CeroFiaoControlField(
                    label = "Notificaciones",
                    description = "Recibir alertas del sistema",
                    control = {
                        CeroFiaoSwitch(checked = switchOn, onCheckedChange = { switchOn = it })
                    }
                )
                CeroFiaoSlider(
                    value = slider,
                    onValueChange = { slider = it },
                    label = { "Intensidad (${(it * 100).toInt()}%)" },
                )
                CeroFiaoSelect(
                    selected = selected,
                    onSelectedChange = { selected = it },
                    options = listOf("USD", "VES", "USDT", "EUR"),
                    label = "Moneda",
                    displayText = { it },
                )
                CeroFiaoRadioGroup(
                    selected = radio,
                    onSelectedChange = { radio = it },
                    options = listOf("BCV", "USDT", "EURI"),
                    label = { it },
                )
                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Text("PIN de seguridad:", fontSize = 12.sp, color = colors.TextSecondary)
                    CeroFiaoInputOTP(
                        length = 4,
                        value = otp,
                        onValueChange = { if (it.length <= 4) otp = it },
                    )
                }

                // ─── CeroFiaoTagLabel ──────────────────────────────────────
                Text("TagLabel — Flat / Solid / Bordered / Faded:", fontSize = 12.sp, color = colors.TextSecondary)
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp),
                ) {
                    CeroFiaoTagLabel(text = "Default")
                    CeroFiaoTagLabel(text = "Primary", color = TagLabelColor.Primary)
                    CeroFiaoTagLabel(text = "Success", color = TagLabelColor.Success)
                    CeroFiaoTagLabel(text = "Warning", color = TagLabelColor.Warning)
                    CeroFiaoTagLabel(text = "Danger",  color = TagLabelColor.Danger)
                    CeroFiaoTagLabel(text = "Solid",   color = TagLabelColor.Primary, variant = TagLabelVariant.Solid)
                    CeroFiaoTagLabel(text = "Bordered", color = TagLabelColor.Success, variant = TagLabelVariant.Bordered)
                    CeroFiaoTagLabel(text = "Faded",   color = TagLabelColor.Warning, variant = TagLabelVariant.Faded)
                    CeroFiaoTagLabel(text = "● Activo", color = TagLabelColor.Success, dot = true)
                    CeroFiaoTagLabel(text = "Beta",    color = TagLabelColor.Primary, size = LabelSize.Sm)
                    CeroFiaoTagLabel(text = "Nuevo",   color = TagLabelColor.Danger,  size = LabelSize.Lg, variant = TagLabelVariant.Solid)
                }
            }
        }

        // ─── Tarjetas ─────────────────────────────────────────────────────────
        Item(title = "Tarjetas", subtitle = "4 variantes · slots compuestos") {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                CeroFiaoCard(variant = CardVariant.Default) {
                    CeroFiaoCardHeader { CeroFiaoCardTitle("Default · Glassmorphism") }
                    CeroFiaoCardBody {
                        CeroFiaoCardDescription("Usa CardBackground con backgroundOpacity del cardConfig.")
                    }
                    CeroFiaoCardFooter {
                        CeroFiaoLinkButton(text = "Acción →", onClick = {})
                    }
                }
                CeroFiaoCard(variant = CardVariant.Secondary) {
                    CeroFiaoCardBody { CeroFiaoCardTitle("Secondary") }
                }
                CeroFiaoCard(variant = CardVariant.Tertiary) {
                    CeroFiaoCardBody { CeroFiaoCardTitle("Tertiary") }
                }
                CeroFiaoCard(variant = CardVariant.Transparent) {
                    CeroFiaoCardBody { CeroFiaoCardTitle("Transparent") }
                }
            }
        }

        // ─── Navegación ───────────────────────────────────────────────────────
        Item(title = "Navegación", subtitle = "Tabs · ListGroup · Accordion") {
            var tab by remember { mutableStateOf(0) }

            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                CeroFiaoTabs(
                    selectedIndex = tab,
                    onSelectedChange = { tab = it },
                    tabs = listOf("Gastos", "Ingresos", "Transferencias"),
                )

                CeroFiaoListGroup(title = "Sección de ejemplo") {
                    Item(
                        title = "Transacciones",
                        subtitle = "Ver historial completo",
                        leading = { CeroFiaoAvatar(initials = "T", size = AvatarSize.Small) },
                        onClick = {}
                    )
                    Item(
                        title = "Cuentas",
                        subtitle = "Administrar cuentas bancarias",
                        leading = {
                            CeroFiaoAvatar(
                                fallbackIcon = Lucide.CreditCard,
                                size = AvatarSize.Small
                            )
                        },
                        onClick = {}
                    )
                    Item(
                        title = "Configuración",
                        leading = {
                            CeroFiaoAvatar(
                                fallbackIcon = Lucide.Settings,
                                size = AvatarSize.Small
                            )
                        },
                        onClick = {}
                    )
                }
            }
        }

        // ─── Overlays ─────────────────────────────────────────────────────────
        Item(title = "Overlays", subtitle = "Dialog · BottomSheet · Toast") {
            var showDialog by remember { mutableStateOf(false) }
            var showSheet by remember { mutableStateOf(false) }
            val toastState = rememberCeroFiaoToastHostState()
            val scope = rememberCoroutineScope()

            FlowRow(
                modifier = Modifier.padding(16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                CeroFiaoButton(onClick = { showDialog = true }, text = "Dialog")
                CeroFiaoButton(onClick = { showSheet = true }, text = "Bottom Sheet", variant = ButtonVariant.Secondary)
                CeroFiaoButton(
                    onClick = {
                        scope.launch {
                            toastState.show(
                                ToastData(
                                    message = "¡Operación completada!",
                                    variant = ToastVariant.Success
                                )
                            )
                        }
                    },
                    text = "Toast",
                    variant = ButtonVariant.Outline
                )
                CeroFiaoButton(
                    onClick = {
                        scope.launch {
                            toastState.show(
                                ToastData(
                                    message = "Error al guardar",
                                    variant = ToastVariant.Danger
                                )
                            )
                        }
                    },
                    text = "Toast Error",
                    variant = ButtonVariant.DangerSoft
                )
            }

            if (showDialog) {
                CeroFiaoDialog(onDismissRequest = { showDialog = false }) {
                    Column(modifier = Modifier.padding(24.dp)) {
                        CeroFiaoCardTitle("CeroFiaoDialog")
                        Spacer(Modifier.height(8.dp))
                        CeroFiaoCardDescription("Diálogo con transiciones animadas y dismiss por gesto.")
                        Spacer(Modifier.height(16.dp))
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            CeroFiaoButton(
                                onClick = { showDialog = false },
                                text = "Cancelar",
                                variant = ButtonVariant.Ghost
                            )
                            CeroFiaoButton(onClick = { showDialog = false }, text = "Confirmar")
                        }
                    }
                }
            }

            if (showSheet) {
                CeroFiaoBottomSheet(onDismissRequest = { showSheet = false }) {
                    Column(modifier = Modifier.padding(horizontal = 24.dp, vertical = 16.dp)) {
                        CeroFiaoCardTitle("CeroFiaoBottomSheet")
                        Spacer(Modifier.height(8.dp))
                        CeroFiaoCardDescription("Desliza hacia abajo para cerrar.")
                        Spacer(Modifier.height(16.dp))
                        CeroFiaoButton(
                            onClick = { showSheet = false },
                            text = "Cerrar",
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(Modifier.height(16.dp))
                    }
                }
            }

            CeroFiaoToastHost(hostState = toastState)
        }

        // ─── Colecciones ──────────────────────────────────────────────────────
        Item(title = "Colecciones", subtitle = "Chip · TagGroup") {
            var selectedTags by remember { mutableStateOf(setOf("USD")) }

            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text("Chips:", fontSize = 12.sp, color = colors.TextSecondary)
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    CeroFiaoChip(label = "Filled", variant = ChipVariant.Filled, onClick = {})
                    CeroFiaoChip(label = "Outlined", variant = ChipVariant.Outlined, onClick = {})
                    CeroFiaoChip(label = "Soft", variant = ChipVariant.Soft, onClick = {})
                    CeroFiaoChip(
                        label = "Removible",
                        variant = ChipVariant.Soft,
                        onDismiss = {},
                        onClick = {}
                    )
                    CeroFiaoChip(
                        label = "Con ícono",
                        icon = Lucide.Bell,
                        variant = ChipVariant.Filled,
                        onClick = {}
                    )
                }

                Text("TagGroup:", fontSize = 12.sp, color = colors.TextSecondary)
                CeroFiaoTagGroup(
                    tags = listOf("USD", "VES", "USDT", "EUR"),
                    selectedTags = selectedTags,
                    onTagSelected = {
                        selectedTags = if (selectedTags.contains(it)) {
                            selectedTags - it
                        } else {
                            selectedTags + it
                        }
                    },
                    displayText = { it },
                )
            }
        }

        // ─── Media & Display ──────────────────────────────────────────────────
        Item(title = "Media & Display", subtitle = "Avatar · Chip con color") {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text("Avatares (S / M / L):", fontSize = 12.sp, color = colors.TextSecondary)
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    CeroFiaoAvatar(initials = "AL", size = AvatarSize.Small)
                    CeroFiaoAvatar(initials = "AL", size = AvatarSize.Medium)
                    CeroFiaoAvatar(initials = "AL", size = AvatarSize.Large)
                    CeroFiaoAvatar(fallbackIcon = Lucide.User, size = AvatarSize.Medium)
                    CeroFiaoAvatar(
                        initials = "CF",
                        size = AvatarSize.Medium,
                        backgroundColor = colors.Primary,
                        contentColor = colors.OnPrimary
                    )
                }

                Text("Chips con color de acento:", fontSize = 12.sp, color = colors.TextSecondary)
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    CeroFiaoChip(label = "Verde", color = colors.AccentGreen, onClick = {})
                    CeroFiaoChip(label = "Rojo", color = colors.AccentRed, onClick = {})
                    CeroFiaoChip(label = "Naranja", color = colors.AccentOrange, onClick = {})
                    CeroFiaoChip(label = "Púrpura", color = colors.AccentPurple, onClick = {})
                }
            }
        }

        // ─── Layout & Utilities ───────────────────────────────────────────────
        Item(title = "Layout & Utilities", subtitle = "Separator · Surface") {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text("Separator horizontal:", fontSize = 12.sp, color = colors.TextSecondary)
                CeroFiaoSeparator()

                Text("Separator con label:", fontSize = 12.sp, color = colors.TextSecondary)
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    CeroFiaoSeparator(modifier = Modifier.weight(1f))
                    Text("o continúa con", fontSize = 11.sp, color = colors.TextSecondary)
                    CeroFiaoSeparator(modifier = Modifier.weight(1f))
                }

                Text("Surface con elevación:", fontSize = 12.sp, color = colors.TextSecondary)
                CeroFiaoSurface(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = "CeroFiaoSurface — elevation + background token",
                        fontSize = 13.sp,
                        color = colors.TextPrimary,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }
        }
    }
}
