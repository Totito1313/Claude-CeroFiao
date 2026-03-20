package com.schwarckdev.cerofiao.core.ui.navigation

import com.schwarckdev.cerofiao.core.designsystem.theme.*
import dev.chrisbanes.haze.*


import androidx.compose.ui.draw.shadow
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ripple
import androidx.compose.animation.*
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.Spring
import androidx.compose.foundation.background
import com.schwarckdev.cerofiao.core.designsystem.components.bounceClick
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.zIndex
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import dev.chrisbanes.haze.HazeState
import dev.chrisbanes.haze.hazeChild
import dev.chrisbanes.haze.HazeTint
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.layout.layout
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.graphics.vector.rememberVectorPainter

import com.schwarckdev.cerofiao.core.designsystem.theme.CeroFiaoDesign
import com.schwarckdev.cerofiao.core.designsystem.theme.CeroFiaoTheme

import com.schwarckdev.cerofiao.core.designsystem.theme.CeroFiaoTextStyles

/**
 * ============================================================================================
 * CeroFiaoTopBar.kt
 * ============================================================================================
 * Este archivo contiene la implementación de la barra superior (TopBar) de la aplicación.
 * Maneja diferentes variantes (Home, Standard, Detail) y sus transiciones.
 * Utiliza efectos de desenfoque (Glassmorphism) y animaciones fluidas estilo iOS/OneUI.
 *
 * Colores Principales usados:
 * - CeroFiaoTheme.tokens.bg: Para el degradado de fondo que se mezcla.
 * - CeroFiaoTheme.tokens.surface: Para los efectos de cristal esmerilado.
 * - CeroFiaoTheme.tokens.text: Para iconos y textos principales.
 * - androidx.compose.ui.graphics.Color.Black: Para las sombras suaves.
 * ============================================================================================
 */

// Define las variantes posibles de la barra superior
enum class TopBarVariant {
    None,            // Barra oculta
    Home,            // Variante principal con saludo (Home Screen)
    Standard,        // Título + (Menu y/o Acciones/Búsqueda) - Usado en secciones generales
    Edit,            // Cancelar + Título + Guardar - Usado en pantallas de edición
    Detail           // Botón Atrás + Título + (Menu y/o Acciones) - Usado en detalles
}

// Clase de datos privada para mapear el estado externo a un estado UI interno más simple
private data class TopBarUiState(
    val variant: TopBarVariant,
    val title: String,
    val subtitle: String?,
    val userName: String?,
    val profilePicture: Any?,
    val rightIcon: Int?,
    val rightIconVector: androidx.compose.ui.graphics.vector.ImageVector?,
    val hasMenu: Boolean,
    val hasSearch: Boolean,
    val duration: String,
    val heartRate: String,
    val onBackClick: (() -> Unit)?,
    val onMenuClick: (() -> Unit)?,
    val onActionClick: (() -> Unit)?,
    val onSearchClick: (() -> Unit)?,
    val onFilterClick: (() -> Unit)?,
    val scrollOffset: Float
)

/**
 * Componente Principal TopBar
 * Decide qué mostrar basándose en el [state] proporcionado.
 * 
 * @param state Estado mutable que controla la variante, título y acciones.
 * @param modifier Modificador para el contenedor raíz.
 * @param hazeState Estado para el efecto de desenfoque (blur) compartido con el contenido scrolleable.
 */
@Composable
fun CeroFiaoTopBar(
    state: CeroFiaoTopBarState,
    modifier: Modifier = Modifier,
    hazeState: HazeState? = null
) {
    // Si no es visible o la variante es None, no renderizamos nada.
    if (!state.isVisible || state.variant == TopBarVariant.None) return

    // Estado local para mostrar/ocultar el menú desplegable (dropdown)
    var showMenu by remember { mutableStateOf(false) }

    // Transformamos el estado externo a nuestro estado UI inmutable
    val uiState = TopBarUiState(
        variant = state.variant,
        title = state.title,
        subtitle = state.subtitle,
        userName = state.userName,
        profilePicture = state.profilePicture,
        rightIcon = state.rightIcon,
        rightIconVector = state.rightIconVector,
        hasMenu = state.menuItems.isNotEmpty() || state.onMenuClick != null,
        hasSearch = state.onSearchClick != null,
        duration = state.duration,
        heartRate = state.heartRate,
        onBackClick = state.onBackClick,
        onMenuClick = state.onMenuClick,
        onActionClick = state.onActionClick,
        onSearchClick = state.onSearchClick,
        onFilterClick = state.onFilterClick,
        scrollOffset = state.scrollOffset
    )

    // =====================================================================
    // LÓGICA DE ACCIONES IZQUIERDA (Left Actions)
    // =====================================================================
    val leftActionState: LeftActionState = when (uiState.variant) {
        TopBarVariant.Detail -> {
            if (uiState.onBackClick != null) LeftActionState.Back(uiState.onBackClick)
            else LeftActionState.None
        }
        TopBarVariant.Edit -> {
            if (uiState.onBackClick != null) LeftActionState.Close(uiState.onBackClick)
            else LeftActionState.None
        }
        TopBarVariant.Home, TopBarVariant.Standard -> {
             if (uiState.onMenuClick != null) {
                 val onMenuAction = {
                     if (state.menuItems.isNotEmpty()) showMenu = true
                     else uiState.onMenuClick?.invoke()
                     Unit
                 }
                 LeftActionState.Menu(onMenuAction)
             }
             else LeftActionState.None
        }
        else -> LeftActionState.None
    }

    val paddingStart by animateDpAsState(
        targetValue = if (leftActionState is LeftActionState.None) 0.dp else 60.dp, 
        label = "StartPadding",
        animationSpec = spring(stiffness = Spring.StiffnessMediumLow)
    )

    // =====================================================================
    // LÓGICA DE ACCIONES DERECHA (Right Actions)
    // =====================================================================
    val rightActionState: RightActionState = when (uiState.variant) {
        TopBarVariant.Home -> {
            if (uiState.onActionClick != null) {
                RightActionState.Multiple(
                    listOf(
                        RightActionItem(
                            type = ActionType.Other,
                            onClick = uiState.onActionClick,
                            icon = {
                                 if (uiState.rightIcon != null) {
                                     Icon(painter = painterResource(id = uiState.rightIcon), contentDescription = null, modifier = Modifier.size(24.dp), tint = CeroFiaoTheme.tokens.text)
                                 } else if (uiState.rightIconVector != null) {
                                     Icon(imageVector = uiState.rightIconVector, contentDescription = null, tint = CeroFiaoTheme.tokens.text)
                                 } else {
                                     Icon(imageVector = Icons.Default.MoreVert, contentDescription = null, tint = CeroFiaoTheme.tokens.text)
                                 }
                            },
                            iconKey = uiState.profilePicture ?: uiState.rightIcon ?: "home_more"
                        )
                    )
                )
            } else RightActionState.None
        }
        TopBarVariant.Standard -> {
            val actions = mutableListOf<RightActionItem>()
            
            if (uiState.hasSearch) {
                actions.add(
                    RightActionItem(
                        type = ActionType.Search,
                        onClick = uiState.onSearchClick!!,
                        icon = { Icon(imageVector = Icons.Default.Search, contentDescription = "Search", modifier = Modifier.size(24.dp), tint = CeroFiaoTheme.tokens.text) },
                        iconKey = "search"
                    )
                )
            }
            
            if (uiState.onFilterClick != null) {
                 actions.add(
                    RightActionItem(
                        type = ActionType.Filter,
                        onClick = uiState.onFilterClick,
                        icon = { Icon(imageVector = Icons.Default.Build, contentDescription = "Filter", modifier = Modifier.size(24.dp), tint = CeroFiaoTheme.tokens.text) },
                        iconKey = "filter"
                    )
                )
            }
            
            if (uiState.onActionClick != null) {
                actions.add(
                    RightActionItem(
                        type = ActionType.Other,
                        onClick = uiState.onActionClick!!,
                        icon = {
                             if (uiState.rightIcon != null) {
                                  Icon(painter = painterResource(id = uiState.rightIcon), contentDescription = null, modifier = Modifier.size(24.dp), tint = CeroFiaoTheme.tokens.text)
                             } else if (uiState.rightIconVector != null) {
                                  Icon(imageVector = uiState.rightIconVector, contentDescription = null, modifier = Modifier.size(24.dp), tint = CeroFiaoTheme.tokens.text)
                             } else {
                                  androidx.compose.material3.Icon(imageVector = Icons.Default.MoreVert, contentDescription = "More", modifier = Modifier.size(24.dp), tint = CeroFiaoTheme.tokens.text)
                             }
                        },
                        iconKey = uiState.rightIcon ?: uiState.rightIconVector ?: "standard_more"
                    )
                )
            }

            if (state.menuItems.isNotEmpty()) {
                actions.add(
                    RightActionItem(
                        type = ActionType.Other,
                        onClick = { showMenu = true },
                        icon = { 
                            androidx.compose.material3.Icon(
                                imageVector = Icons.Default.MoreVert, 
                                contentDescription = "More", 
                                modifier = Modifier.size(24.dp), 
                                tint = CeroFiaoTheme.tokens.text
                            ) 
                        },
                        iconKey = "standard_more"
                    )
                )
            }

            if (actions.isNotEmpty()) RightActionState.Multiple(actions) else RightActionState.None
        }
        TopBarVariant.Edit -> {
             if (uiState.onActionClick != null) {
                  RightActionState.Multiple(
                      listOf(
                          RightActionItem(
                              type = ActionType.Other,
                              onClick = uiState.onActionClick,
                              icon = { Icon(imageVector = Icons.Default.Check, contentDescription = "Guardar", modifier = Modifier.size(24.dp), tint = CeroFiaoTheme.tokens.text) },
                              iconKey = "edit_save"
                          )
                      )
                  )
             } else RightActionState.None
        }
        TopBarVariant.Detail -> {
             val actions = mutableListOf<RightActionItem>()
             
             if (uiState.onActionClick != null) {
                  actions.add(
                      RightActionItem(
                          type = ActionType.Other,
                          onClick = uiState.onActionClick,
                          icon = { 
                              if (uiState.rightIcon != null) Icon(painter = painterResource(id = uiState.rightIcon), contentDescription = null, modifier = Modifier.size(24.dp), tint = CeroFiaoTheme.tokens.text)
                              else if (uiState.rightIconVector != null) Icon(imageVector = uiState.rightIconVector, contentDescription = null, modifier = Modifier.size(24.dp), tint = CeroFiaoTheme.tokens.text)
                              else Icon(imageVector = Icons.Default.Check, contentDescription = null, modifier = Modifier.size(24.dp), tint = CeroFiaoTheme.tokens.text)
                          },
                          iconKey = uiState.rightIcon ?: "detail_check"
                      )
                  )
             }
             
             actions.add(
                 RightActionItem(
                    type = ActionType.Other,
                    onClick = {
                        if (state.menuItems.isNotEmpty()) showMenu = true
                        else uiState.onMenuClick?.invoke()
                    },
                    icon = { androidx.compose.material3.Icon(imageVector = Icons.Default.MoreVert, contentDescription = "More", modifier = Modifier.size(24.dp), tint = CeroFiaoTheme.tokens.text) },
                    iconKey = "detail_more"
                 )
             )
             
             if (actions.isNotEmpty()) RightActionState.Multiple(actions) else RightActionState.None
        }
        else -> RightActionState.None
    }

    // =====================================================================
    // RENDERIZADO DEL CONTENEDOR PRINCIPAL
    // =====================================================================
    Box(modifier = modifier.fillMaxWidth().graphicsLayer(clip = false)) {
        
        // 1. ANIMATED CONTENT (HEADERS)
        AnimatedContent(
            targetState = uiState,
            contentKey = { it.variant },
            modifier = Modifier
                .graphicsLayer(clip = false),
            transitionSpec = {
                fadeIn(animationSpec = tween(300)) togetherWith fadeOut(animationSpec = tween(300))
            },
            label = "TopBarTransition"
        ) { targetState ->
            when (targetState.variant) {
                TopBarVariant.None -> {} 
                
                TopBarVariant.Home -> GradientHeader(
                    title = "Inicio", // Hardcoded for now if not provided
                    userName = targetState.userName,
                    showUserSubtitle = true,
                    profilePicture = null,
                    onMenuClick = null,
                    onActionClick = null,
                    startPadding = paddingStart,
                    hazeState = hazeState,
                    scrollOffset = targetState.scrollOffset
                )

                TopBarVariant.Standard -> StandardHeader(
                    title = targetState.title,
                    subtitle = targetState.subtitle,
                    hasSearch = false,
                    onSearchClick = null,
                    hasMenu = false, 
                    onMenuClick = null, 
                    onActionClick = null,
                    rightIcon = null,
                    rightIconVector = null,
                    profilePicture = null,
                    startPadding = paddingStart,
                    hazeState = hazeState,
                    scrollOffset = targetState.scrollOffset
                )

                TopBarVariant.Edit -> DetailHeader(
                    title = targetState.title,
                    subtitle = targetState.subtitle,
                    onBackClick = null,
                    onMenuClick = null,
                    onActionClick = null,
                    rightIcon = null,
                    rightIconVector = null,
                    startPadding = paddingStart,
                    hazeState = hazeState,
                    scrollOffset = targetState.scrollOffset
                )

                TopBarVariant.Detail -> DetailHeader(
                    title = targetState.title,
                    subtitle = targetState.subtitle,
                    onBackClick = null,
                    onMenuClick = null,
                    onActionClick = null,
                    rightIcon = null,
                    rightIconVector = null,
                    startPadding = paddingStart,
                    hazeState = hazeState,
                    scrollOffset = targetState.scrollOffset
                )
            }
        }
        
        // 2. OVERLAY LEFT ACTIONS
        Box(
            modifier = Modifier
                .align(Alignment.TopStart)
                .zIndex(1f)
                .padding(start = 16.dp)
                .graphicsLayer(clip = false)
                .windowInsetsPadding(WindowInsets.statusBars)
                .padding(top = 7.dp)
        ) {
            AnimatedLeftActions(state = leftActionState, hazeState = hazeState, scrollOffset = uiState.scrollOffset)
        }

        // 3. OVERLAY RIGHT ACTIONS
        Box(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(end = 18.dp)
                .graphicsLayer(clip = false)
                .windowInsetsPadding(WindowInsets.statusBars)
                .padding(top = 7.dp) 
        ) {
            AnimatedRightActions(state = rightActionState, hazeState = hazeState, scrollOffset = uiState.scrollOffset)
        }

        // 4. CUSTOM DROPDOWN MENU
        if (state.menuItems.isNotEmpty()) {
            
            AnimatedVisibility(
                visible = showMenu,
                enter = fadeIn(),
                exit = fadeOut(),
                modifier = Modifier.zIndex(100f)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null
                        ) { showMenu = false }
                )
            }
            
            AnimatedVisibility(
                visible = showMenu,
                enter = fadeIn(animationSpec = tween(durationMillis = 150)) + 
                        scaleIn(
                            initialScale = 0.5f,
                            transformOrigin = TransformOrigin(1f, 0f),
                            animationSpec = spring(dampingRatio = 0.7f, stiffness = Spring.StiffnessMediumLow)
                        ),
                exit = fadeOut(animationSpec = tween(durationMillis = 100)) + 
                       scaleOut(
                           targetScale = 0.5f,
                           transformOrigin = TransformOrigin(1f, 0f),
                           animationSpec = tween(durationMillis = 100)
                       ),
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .zIndex(101f)
                    .windowInsetsPadding(WindowInsets.statusBars)
                    .padding(end = 16.dp)
                    .offset(y = (6).dp)
            ) {
                androidx.compose.material3.Surface(
                    shape = RoundedCornerShape(26.dp),
                    color = CeroFiaoTheme.tokens.surface,
                    shadowElevation = 8.dp,
                    modifier = Modifier.width(160.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(vertical = 8.dp)
                    ) {
                        val defaultIconColor = CeroFiaoTheme.tokens.textSecondary
                        state.menuItems.forEach { item ->
                            // Implement basic DropdownMenuItem logic since we don't have LionDropdownMenuItem
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        showMenu = false
                                        item.onClick()
                                    }
                                    .padding(horizontal = 16.dp, vertical = 12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                if (item.icon != null) {
                                    Icon(
                                        imageVector = item.icon,
                                        contentDescription = null,
                                        tint = if (item.color != Color.Unspecified) item.color else defaultIconColor,
                                        modifier = Modifier.size(20.dp)
                                    )
                                    Spacer(modifier = Modifier.width(12.dp))
                                }
                                Text(
                                    text = item.text,
                                    color = if (item.color != Color.Unspecified) item.color else CeroFiaoTheme.tokens.text,
                                    style = MaterialTheme.typography.bodyLarge
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

// =====================================================================
// DEFINICIÓN DE TIPOS Y ESTADOS PARA ACCIONES
// =====================================================================
private enum class ActionType { Search, Filter, Other }

private sealed interface LeftActionState {
    data object None : LeftActionState
    data class Back(val onClick: () -> Unit) : LeftActionState
    data class Menu(val onClick: () -> Unit) : LeftActionState
    data class Close(val onClick: () -> Unit) : LeftActionState
}

@Composable
private fun AnimatedLeftActions(
    state: LeftActionState,
    modifier: Modifier = Modifier,
    hazeState: HazeState? = null,
    scrollOffset: Float = 0f
) {
    val isVisible = state !is LeftActionState.None
    
    val scrollTranslationY = remember(scrollOffset) { 
        -(scrollOffset * 0.5f).coerceIn(0f, 200f) 
    }
    
    val scrollAlpha = remember(scrollOffset) {
         (1f - (scrollOffset / 300f)).coerceIn(0f, 1f)
    }

    val offsetX by animateDpAsState(
        targetValue = if (isVisible) 0.dp else (-20).dp,
        label = "LeftActionOffset",
        animationSpec = spring(stiffness = Spring.StiffnessMediumLow)
    )

    val structuralAlpha by animateFloatAsState(
        targetValue = if (isVisible) 1f else 0f,
        label = "LeftActionAlpha",
        animationSpec = tween(200)
    )
    
    val finalAlpha = structuralAlpha * scrollAlpha
    
    if (!isVisible && structuralAlpha == 0f) return

    val shadowPadding = 40.dp 
    
    Box(
        modifier = modifier
            .offset(x = offsetX - shadowPadding, y = -shadowPadding)
            .graphicsLayer {
                translationY = scrollTranslationY
                alpha = finalAlpha
                clip = false
            } 
            .layout { measurable, constraints ->
                val paddingPx = shadowPadding.roundToPx()
                val placeable = measurable.measure(constraints)
                layout(placeable.width + paddingPx * 2, placeable.height + paddingPx * 2) {
                    placeable.place(paddingPx, paddingPx)
                }
            }
    ) {
         when (state) {
             is LeftActionState.Back -> {
                 GlassButton(onClick = state.onClick, hazeState = hazeState) {
                    Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back", modifier = Modifier.size(24.dp), tint = CeroFiaoTheme.tokens.text)
                 }
             }
              is LeftActionState.Menu -> {
                  GlassButton(onClick = state.onClick, hazeState = hazeState) {
                      Icon(imageVector = Icons.Default.Menu, contentDescription = "Menu", modifier = Modifier.size(24.dp), tint = CeroFiaoTheme.tokens.text)
                  }
              }
              is LeftActionState.Close -> {
                  GlassButton(onClick = state.onClick, hazeState = hazeState) {
                      Icon(imageVector = Icons.Default.Close, contentDescription = "Close", modifier = Modifier.size(24.dp), tint = CeroFiaoTheme.tokens.text)
                  }
              }
              is LeftActionState.None -> {}
         }
    }
}

private data class RightActionItem(
    val type: ActionType,
    val onClick: () -> Unit,
    val icon: @Composable () -> Unit,
    val iconKey: Any? = null
)

private sealed interface RightActionState {
    data object None : RightActionState
    data class Multiple(val actions: List<RightActionItem>) : RightActionState
}

@Composable
private fun AnimatedRightActions(
    state: RightActionState,
    modifier: Modifier = Modifier,
    hazeState: HazeState? = null,
    scrollOffset: Float = 0f
) {
    val items = if (state is RightActionState.Multiple) state.actions else emptyList()
    val count = items.size
    
    val scrollTranslationY = remember(scrollOffset) { 
        -(scrollOffset * 0.5f).coerceIn(0f, 200f) 
    }
    
    val scrollAlpha = remember(scrollOffset) {
         (1f - (scrollOffset / 300f)).coerceIn(0f, 1f)
    }

    val totalWidth by animateDpAsState(
        targetValue = if (count > 0) (count * 50).dp else 0.dp,
        label = "TotalWidth",
        animationSpec = spring(stiffness = Spring.StiffnessMediumLow)
    )

    if (totalWidth < 1.dp) return

    val shadowPadding = 40.dp
    
    Box(
        modifier = modifier
             .offset(x = shadowPadding, y = -shadowPadding)
             .graphicsLayer {
                 translationY = scrollTranslationY
                 alpha = scrollAlpha
                 clip = false
             }
             .layout { measurable, constraints ->
                val paddingPx = shadowPadding.roundToPx()
                val placeable = measurable.measure(constraints)
                layout(placeable.width + paddingPx * 2, placeable.height + paddingPx * 2) {
                    placeable.place(paddingPx, paddingPx)
                }
             }
             .shadow(15.dp, androidx.compose.foundation.shape.RoundedCornerShape(25.dp))
    ) {
        val bgAlpha = 0.15f
    
        Box(
            modifier = Modifier
                .size(width = totalWidth, height = 50.dp)
                .clip(RoundedCornerShape(25.dp))
                .then(
                    if (hazeState != null) {
                        Modifier.hazeChild(
                            state = hazeState, 
                            style = dev.chrisbanes.haze.HazeStyle(
                                backgroundColor = CeroFiaoTheme.tokens.surface,
                                blurRadius = 24.dp,
                                tint = null
                            )
                        )
                    } else Modifier
                )
                .background(CeroFiaoTheme.tokens.surface.copy(alpha = 0.8f))
                .border(1.dp, CeroFiaoTheme.tokens.surfaceBorder.copy(alpha = 0.2f), RoundedCornerShape(25.dp))
        ) {
            Row(modifier = Modifier.fillMaxSize()) {
                items.forEachIndexed { index, item ->
                     Box(
                         modifier = Modifier
                            .width(50.dp)
                            .fillMaxHeight()
                            .clip(RectangleShape)
                     ) {
                         Box(
                             modifier = Modifier
                                 .fillMaxSize()
                                 .padding(4.dp)
                                 .clip(CircleShape)
                                 .bounceClick(onClick = item.onClick),
                             contentAlignment = Alignment.Center
                         ) {
                               item.icon()
                         }
                     }
                }
            }
        }
    }
}

/**
 * Botón Glassmórfico (GlassButton)
 */
@Composable
fun GlassButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
    hazeState: HazeState? = null,
    content: @Composable () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val scale by animateFloatAsState(targetValue = if (isPressed) 0.95f else 1f, label = "scale")

    Box(
        modifier = modifier
            .scale(scale)
            .size(50.dp)
            .shadow(15.dp, androidx.compose.foundation.shape.RoundedCornerShape(25.dp))
    ) {
        val bgAlpha = 0.15f

        Box(
            modifier = Modifier
                .fillMaxSize()
                .clip(CircleShape)
                .then(
                    if (hazeState != null) {
                        Modifier.hazeChild(
                            state = hazeState, 
                            style = dev.chrisbanes.haze.HazeStyle(
                                backgroundColor = CeroFiaoTheme.tokens.surface,
                                blurRadius = 24.dp,
                                tint = null
                            )
                        )
                    } else Modifier
                )
                .background(CeroFiaoTheme.tokens.surface.copy(alpha = 0.8f))
                .border(1.dp, CeroFiaoTheme.tokens.surfaceBorder.copy(alpha = 0.2f), CircleShape)
                .clip(CircleShape)
                .bounceClick(onClick = onClick),
            contentAlignment = Alignment.Center
        ) {
            content()
        }
    }
}

/**
 * Contenedor de Título animado.
 */
@Composable
fun TitleContainer(title: String, subtitle: String? = null, scrollOffset: Float = 0f) {
    val translationY = remember(scrollOffset) { 
        -(scrollOffset * 0.5f).coerceIn(0f, 200f) 
    }
    val alpha = remember(scrollOffset) {
        (1f - (scrollOffset / 300f)).coerceIn(0f, 1f)
    }

    Column(
        modifier = Modifier
            .padding(start = 8.dp)
            .graphicsLayer {
                this.translationY = translationY
                this.alpha = alpha
            },
        verticalArrangement = Arrangement.Center
    ) {
        AnimatedContent(
            targetState = title,
            transitionSpec = {
                fadeIn(animationSpec = tween(300)) togetherWith fadeOut(animationSpec = tween(300))
            },
            label = "TitleAnimation"
        ) { targetTitle ->
            Text(
                text = targetTitle,
                style = MaterialTheme.typography.headlineMedium,
                color = CeroFiaoTheme.tokens.text
            )
        }
        val truncatedSubtitle = subtitle?.let { if (it.length > 30) it.take(30) + "..." else it }
        AnimatedVisibility(
            visible = !truncatedSubtitle.isNullOrBlank(),
            enter = fadeIn() + expandVertically(),
            exit = fadeOut() + shrinkVertically()
        ) {
            if (truncatedSubtitle != null) {
                Text(
                    text = truncatedSubtitle,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Normal,
                    color = CeroFiaoTheme.tokens.textSecondary,
                    lineHeight = 14.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

// advancedShadow is imported from com.SchwarckDev.cerofiao.core.designsystem.components.Modifiers

// =====================================================================
// SUB-COMPONENTES (Headers específicos)
// =====================================================================

@Composable
fun StandardHeaderContainer(
    startPadding: Dp = 0.dp,
    hazeState: HazeState? = null,
    content: @Composable RowScope.() -> Unit
) {
    Box(modifier = Modifier.fillMaxWidth()) {
        val gradientColor = CeroFiaoTheme.tokens.bg
        
        Box(
             modifier = Modifier
                .fillMaxWidth()
                .height(120.dp)
                .align(Alignment.TopCenter)
                .graphicsLayer { alpha = 0.99f }
                .drawWithContent {
                    drawContent()
                    drawRect(
                        brush = Brush.verticalGradient(
                            0.0f to gradientColor,
                            0.5f to gradientColor,
                            1.0f to Color.Transparent,
                            startY = 0f,
                            endY = size.height
                        ),
                        blendMode = BlendMode.DstIn
                    )
                }
                .then(
                    if (hazeState != null) {
                        Modifier.hazeChild(
                            state = hazeState, 
                            style = dev.chrisbanes.haze.HazeStyle(
                                backgroundColor = CeroFiaoTheme.tokens.bg,
                                blurRadius = 80.dp,
                                tint = HazeTint(CeroFiaoTheme.tokens.bg.copy(alpha = 0.6f))
                            )
                        )
                    } else {
                        Modifier.blur(30.dp)
                    }
                )
                .background(
                    brush = Brush.verticalGradient(
                        0.0f to CeroFiaoTheme.tokens.bg.copy(alpha = if (hazeState != null) 0.8f else 1f),
                        0.65f to CeroFiaoTheme.tokens.bg.copy(alpha = if (hazeState != null) 0.8f else 1f),
                        0.85f to CeroFiaoTheme.tokens.bg.copy(alpha = if (hazeState != null) 0.6f else 0.8f),
                        1.5f to CeroFiaoTheme.tokens.bg.copy(alpha = 0f) 
                    )
                )
        )

        Column {
            Spacer(modifier = Modifier.windowInsetsTopHeight(WindowInsets.statusBars))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(64.dp) 
                    .padding(start = startPadding)
                    .padding(horizontal = 18.dp, vertical = 4.dp), 
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                content = content
            )
        }
    }
}

@Composable
fun StandardHeader(
    title: String,
    subtitle: String? = null,
    hasSearch: Boolean = false,
    onSearchClick: (() -> Unit)? = null,
    hasMenu: Boolean = false,
    onMenuClick: (() -> Unit)? = null,
    onActionClick: (() -> Unit)? = null,
    rightIcon: Int? = null,
    rightIconVector: androidx.compose.ui.graphics.vector.ImageVector? = null,
    profilePicture: Any? = null,
    startPadding: Dp = 0.dp,
    hazeState: HazeState? = null,
    scrollOffset: Float = 0f
) {
    Box(
        modifier = Modifier.fillMaxWidth()
    ) {
        val gradientColor = CeroFiaoTheme.tokens.bg
        val scrollAlpha = remember(scrollOffset) {
             (1f - (scrollOffset / 300f)).coerceIn(0f, 1f)
        }
         
         Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp)
                .align(Alignment.TopCenter)
                .zIndex(-1f)
                .graphicsLayer { alpha = 0.99f * scrollAlpha }
                .drawWithContent {
                    drawContent()
                    drawRect(
                        brush = Brush.verticalGradient(
                            0.0f to gradientColor,
                            0.2f to gradientColor,
                            1.0f to Color.Transparent,
                            startY = 0f,
                            endY = size.height
                        ),
                        blendMode = BlendMode.DstIn
                    )
                }
                .then(
                    if (hazeState != null) {
                        Modifier.hazeChild(
                            state = hazeState, 
                            style = dev.chrisbanes.haze.HazeStyle(
                                backgroundColor = CeroFiaoTheme.tokens.bg,
                                blurRadius = 30.dp,
                                tint = HazeTint(CeroFiaoTheme.tokens.bg.copy(alpha = 0.2f))
                            )
                        )
                    } else {
                        Modifier.blur(90.dp)
                    }
                )
                .background(
                    brush = Brush.verticalGradient(
                        0.0f to CeroFiaoTheme.tokens.bg.copy(alpha = if (hazeState != null) 0.8f else 1f),
                        0.65f to CeroFiaoTheme.tokens.bg.copy(alpha = if (hazeState != null) 0.8f else 1f),
                        0.85f to CeroFiaoTheme.tokens.bg.copy(alpha = if (hazeState != null) 0.6f else 0.8f),
                        1.5f to CeroFiaoTheme.tokens.bg.copy(alpha = 0f) 
                    )
                )
        )

        Column {
            Spacer(modifier = Modifier.windowInsetsTopHeight(WindowInsets.statusBars))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(64.dp)
                    .graphicsLayer(clip = false)
                    .padding(start = startPadding)
                    .padding(horizontal = 18.dp, vertical = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
             Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                 if (hasMenu) {
                     Box(
                         modifier = Modifier
                             .wrapContentSize(unbounded = true)
                             .graphicsLayer(clip = false)
                     ) {
                         GlassButton(onClick = onMenuClick ?: {}, hazeState = hazeState) {
                             Icon(
                                imageVector = Icons.Default.Menu,
                                contentDescription = "Menu",
                                modifier = Modifier.size(24.dp),
                                tint = CeroFiaoTheme.tokens.text
                            )
                         }
                     }
                     Spacer(modifier = Modifier.width(10.dp))
                 }
                 TitleContainer(title, subtitle, scrollOffset = scrollOffset)
             }
            }
        }
    }
}

@Composable
fun DetailHeader(
    title: String, 
    subtitle: String? = null,
    onBackClick: (() -> Unit)? = null, 
    onMenuClick: (() -> Unit)? = null,
    onActionClick: (() -> Unit)? = null,
    rightIcon: Int? = null,
    rightIconVector: androidx.compose.ui.graphics.vector.ImageVector? = null,
    startPadding: Dp = 0.dp,
    hazeState: HazeState? = null,
    scrollOffset: Float = 0f
) {
    StandardHeaderContainer(startPadding = startPadding, hazeState = hazeState) {
        Row(modifier = Modifier.weight(1f), verticalAlignment = Alignment.CenterVertically) {
            if (onBackClick != null) {
                GlassButton(onClick = onBackClick, hazeState = hazeState) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Back",
                        modifier = Modifier.size(24.dp),
                        tint = CeroFiaoTheme.tokens.text
                    )
                }
                Spacer(modifier = Modifier.width(10.dp))
            }
            TitleContainer(title = title, subtitle = subtitle, scrollOffset = scrollOffset)
        }
    }
}

@Composable
fun GradientHeader(
    title: String,
    userName: String? = null,
    showUserSubtitle: Boolean,
    profilePicture: Any? = null,
    onActionClick: (() -> Unit)? = null,
    onMenuClick: (() -> Unit)? = null,
    startPadding: Dp = 0.dp,
    hazeState: HazeState? = null,
    scrollOffset: Float = 0f
) {
    StandardHeader(
        title = title,
        subtitle = if (showUserSubtitle) "Hola, ${userName ?: ""}" else null,
        onActionClick = onActionClick,
        profilePicture = profilePicture,
        hasMenu = onMenuClick != null,
        onMenuClick = onMenuClick,
        startPadding = startPadding,
        hazeState = hazeState,
        scrollOffset = scrollOffset
    )
}

// =====================================================================
// MODIFICADORES DE INTERACCIÓN (Bounce + Haptics)
// =====================================================================
@Composable
fun Modifier.bounceClick(
    onClick: () -> Unit,
    scaleDown: Float = 0.90f
): Modifier {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val scale by animateFloatAsState(
        targetValue = if (isPressed) scaleDown else 1f,
        label = "BounceAnimation",
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy, stiffness = Spring.StiffnessMedium)
    )
    val haptic = LocalHapticFeedback.current

    return this
        .graphicsLayer {
            scaleX = scale
            scaleY = scale
        }
        .clickable(
            interactionSource = interactionSource,
            indication = ripple(), 
            onClick = {
                // Assuming haptics are enabled globally for now in CeroFiao
                haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove) 
                onClick()
            }
        )
}

 
