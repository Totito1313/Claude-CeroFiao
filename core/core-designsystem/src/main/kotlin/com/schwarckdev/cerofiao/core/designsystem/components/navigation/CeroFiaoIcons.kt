package com.schwarckdev.cerofiao.core.designsystem.components.navigation

import androidx.annotation.DrawableRes
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathFillType
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.PathParser
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp
import com.composables.icons.lucide.*
import dev.oneuiproject.oneui.R as IconsR

object CeroFiaoIcons {

    val Scan: ImageVector
        get() = ImageVector.Builder(
            name = "Scan",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).apply {
            path(
                fill = SolidColor(Color.Black),
                pathFillType = PathFillType.EvenOdd
            ) {
                moveTo(5f, 5f)
                horizontalLineTo(9f)
                verticalLineTo(3f)
                horizontalLineTo(5f)
                curveTo(3.895f, 3f, 3f, 3.895f, 3f, 5f)
                verticalLineTo(9f)
                horizontalLineTo(5f)
                verticalLineTo(5f)
                close()
                moveTo(15f, 3f)
                horizontalLineTo(19f)
                curveTo(20.105f, 3f, 21f, 3.895f, 21f, 5f)
                verticalLineTo(9f)
                horizontalLineTo(19f)
                verticalLineTo(5f)
                horizontalLineTo(15f)
                verticalLineTo(3f)
                close()
                moveTo(19f, 15f)
                horizontalLineTo(21f)
                verticalLineTo(19f)
                curveTo(21f, 20.105f, 20.105f, 21f, 19f, 21f)
                horizontalLineTo(15f)
                verticalLineTo(19f)
                horizontalLineTo(19f)
                verticalLineTo(15f)
                close()
                moveTo(5f, 19f)
                verticalLineTo(15f)
                horizontalLineTo(3f)
                verticalLineTo(19f)
                curveTo(3f, 20.105f, 3.895f, 21f, 5f, 21f)
                horizontalLineTo(9f)
                verticalLineTo(19f)
                horizontalLineTo(5f)
                close()
                moveTo(6f, 11f)
                horizontalLineTo(18f)
                verticalLineTo(13f)
                horizontalLineTo(6f)
                verticalLineTo(11f)
                close()
            }
        }.build()

    val Bolt: ImageVector
        get() = ImageVector.Builder(
            name = "Bolt",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).apply {
            // Path 1
            path(
                fill = SolidColor(Color.White),
                fillAlpha = 1f,
                stroke = null,
                strokeAlpha = 1f,
                strokeLineWidth = 1.0f,
                strokeLineCap = StrokeCap.Butt,
                strokeLineJoin = StrokeJoin.Miter,
                strokeLineMiter = 1.0f,
                pathFillType = PathFillType.EvenOdd
            ) {
                moveTo(13.3729f, 10.0288f)
                lineTo(16.9243f, 10.0289f)
                lineTo(6.43799f, 21.0519f)
                lineTo(10.3823f, 12.2869f)
                lineTo(13.3729f, 10.0288f)
                close()
            }
            // Path 2
            path(
                fill = SolidColor(Color.White),
                fillAlpha = 1f,
                stroke = null,
                pathFillType = PathFillType.EvenOdd
            ) {
                moveTo(8.8172f, 3.5f)
                lineTo(6f, 12.2871f)
                horizontalLineTo(10.3824f)
                lineTo(14.966f, 3.5f)
                horizontalLineTo(8.8172f)
                close()
            }
            // Path 3
            path(
                fill = SolidColor(Color.White),
                fillAlpha = 1f,
                stroke = null,
                pathFillType = PathFillType.EvenOdd
            ) {
                moveTo(10.3823f, 12.287f)
                lineTo(13.3729f, 10.0287f)
                horizontalLineTo(11.5581f)
                lineTo(10.3823f, 12.287f)
                close()
            }
        }.build()

    val Time: ImageVector
        get() = ImageVector.Builder(
            name = "Time",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).apply {
            // Path 1 (Blue Circle)
            path(
                fill = SolidColor(Color(0xFF64C5F9)),
                pathFillType = PathFillType.EvenOdd
            ) {
                moveTo(18.9f, 12f)
                curveTo(18.9f, 13.83f, 18.173f, 15.585f, 16.879f, 16.879f)
                curveTo(15.585f, 18.173f, 13.83f, 18.9f, 12f, 18.9f)
                curveTo(10.17f, 18.9f, 8.41497f, 18.173f, 7.12097f, 16.879f)
                curveTo(5.82697f, 15.585f, 5.10001f, 13.83f, 5.10001f, 12f)
                curveTo(5.10001f, 10.17f, 5.82697f, 8.41494f, 7.12097f, 7.12094f)
                curveTo(8.41497f, 5.82694f, 10.17f, 5.09998f, 12f, 5.09998f)
                curveTo(13.83f, 5.09998f, 15.585f, 5.82694f, 16.879f, 7.12094f)
                curveTo(18.173f, 8.41494f, 18.9f, 10.17f, 18.9f, 12f)
                close()
            }
            // Path 2 (White Detail 1)
            path(
                fill = SolidColor(Color(0xFFFCFCFC)),
                pathFillType = PathFillType.EvenOdd
            ) {
                moveTo(15.696f, 8.30102f)
                curveTo(15.495f, 8.10002f, 15.1695f, 8.10002f, 14.9685f, 8.30102f)
                lineTo(11.6955f, 11.574f)
                curveTo(11.4945f, 11.775f, 11.4945f, 12.1005f, 11.6955f, 12.3015f)
                curveTo(11.8965f, 12.5025f, 12.222f, 12.5025f, 12.423f, 12.3015f)
                lineTo(15.696f, 9.02852f)
                curveTo(15.8955f, 8.82752f, 15.8955f, 8.50202f, 15.696f, 8.30102f)
                close()
            }
            // Path 3 (White Detail 2)
            path(
                fill = SolidColor(Color(0xFFFCFCFC)),
                pathFillType = PathFillType.EvenOdd
            ) {
                moveTo(9.36599f, 9.36596f)
                curveTo(9.56699f, 9.16496f, 9.89249f, 9.16496f, 10.0935f, 9.36596f)
                lineTo(12.276f, 11.5485f)
                curveTo(12.4755f, 11.7495f, 12.4755f, 12.075f, 12.276f, 12.276f)
                curveTo(12.075f, 12.477f, 11.7495f, 12.477f, 11.5485f, 12.276f)
                lineTo(9.36599f, 10.0935f)
                curveTo(9.16499f, 9.89246f, 9.16499f, 9.56696f, 9.36599f, 9.36596f)
                close()
            }
            // Path 4 (Gray Dot)
            path(
                fill = SolidColor(Color(0xFF595959))
            ) {
                moveTo(12f, 11.4f)
                curveTo(11.6685f, 11.4f, 11.4f, 11.6685f, 11.4f, 12f)
                curveTo(11.4f, 12.3315f, 11.6685f, 12.6f, 12f, 12.6f)
                curveTo(12.3315f, 12.6f, 12.6f, 12.3315f, 12.6f, 12f)
                curveTo(12.6f, 11.6685f, 12.3315f, 11.4f, 12f, 11.4f)
                close()
            }
        }.build()

    val Play: ImageVector
        get() = ImageVector.Builder(
            name = "Play",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).apply {
            path(
                fill = SolidColor(Color.White),
                pathFillType = PathFillType.EvenOdd
            ) {
                moveTo(8.762f, 19.519f)
                lineTo(18.011f, 13.476f)
                curveTo(19.148f, 12.82f, 19.148f, 11.18f, 18.011f, 10.524f)
                lineTo(8.762f, 4.481f)
                curveTo(7.626f, 3.825f, 6.205f, 4.645f, 6.205f, 5.957f)
                verticalLineTo(18.042f)
                curveTo(6.205f, 19.355f, 7.626f, 20.175f, 8.762f, 19.519f)
                close()
            }
        }.build()

    val Sports: ImageVector
        get() = ImageVector.Builder(
            name = "Sports",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).apply {
            path(
                stroke = SolidColor(Color.White),
                strokeLineWidth = 1.5f
            ) {
                moveTo(20.75f, 12.375f)
                curveTo(20.75f, 17f, 17f, 20.75f, 12.375f, 20.75f)
                moveTo(20.75f, 12.375f)
                curveTo(20.75f, 7.75f, 17f, 4f, 12.375f, 4f)
                moveTo(20.75f, 12.375f)
                horizontalLineTo(4f)
                moveTo(12.375f, 20.75f)
                curveTo(7.75f, 20.75f, 4f, 17f, 4f, 12.375f)
                moveTo(12.375f, 20.75f)
                verticalLineTo(4f)
                moveTo(4f, 12.375f)
                curveTo(4f, 7.75f, 7.75f, 4f, 12.375f, 4f)
                moveTo(18.209f, 6.82f)
                curveTo(17.919f, 7.346f, 17.461f, 8.054f, 17.049f, 8.696f)
                curveTo(16.152f, 10.091f, 15.745f, 10.929f, 15.745f, 12.374f)
                curveTo(15.745f, 13.82f, 16.152f, 14.659f, 17.047f, 16.055f)
                curveTo(17.478f, 16.723f, 17.96f, 17.466f, 18.246f, 17.996f)
                moveTo(6.46f, 6.669f)
                curveTo(6.74f, 7.206f, 7.248f, 7.991f, 7.702f, 8.696f)
                curveTo(8.599f, 10.091f, 9.006f, 10.93f, 9.005f, 12.374f)
                curveTo(9.005f, 13.82f, 8.599f, 14.659f, 7.702f, 16.055f)
                curveTo(7.272f, 16.723f, 6.79f, 17.466f, 6.504f, 17.996f)
            }
        }.build()

    val TimeOutline: ImageVector
        get() = ImageVector.Builder(
            name = "TimeOutline",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).apply {
            path(
                stroke = SolidColor(Color.White),
                strokeLineWidth = 1.5f,
                strokeLineCap = StrokeCap.Round,
                strokeLineJoin = StrokeJoin.Round
            ) {
                moveTo(12f, 7.5957f)
                verticalLineTo(11.3657f)
                curveTo(12f, 11.6457f, 12.227f, 11.8737f, 12.507f, 11.8737f)
                horizontalLineTo(15.576f)
                moveTo(20.5f, 12f)
                curveTo(20.5f, 7.305f, 16.695f, 3.5f, 12f, 3.5f)
                curveTo(7.306f, 3.5f, 3.5f, 7.305f, 3.5f, 12f)
                curveTo(3.5f, 16.695f, 7.306f, 20.5f, 12f, 20.5f)
                curveTo(16.695f, 20.5f, 20.5f, 16.695f, 20.5f, 12f)
                close()
            }
        }.build()

    val Workout: ImageVector
        get() = ImageVector.Builder(
            name = "Workout",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).apply {
            path(
                fill = SolidColor(Color.White),
                pathFillType = PathFillType.EvenOdd
            ) {
                moveTo(9.4937f, 9.4572f)
                curveTo(9.8707f, 9.4392f, 10.2497f, 9.4912f, 10.5637f, 9.6772f)
                lineTo(20.6467f, 16.3622f)
                curveTo(20.9967f, 16.5512f, 21.2347f, 16.8982f, 21.2817f, 17.2912f)
                curveTo(21.3627f, 17.9602f, 20.8847f, 18.5702f, 20.2167f, 18.6502f)
                curveTo(19.9457f, 18.6842f, 19.6787f, 18.6272f, 19.4447f, 18.4852f)
                lineTo(13.3887f, 14.4872f)
                lineTo(12.4147f, 16.1042f)
                lineTo(15.0847f, 16.8192f)
                curveTo(15.5327f, 16.9672f, 15.8587f, 17.3652f, 15.9157f, 17.8332f)
                curveTo(15.9957f, 18.5012f, 15.5167f, 19.1102f, 14.8487f, 19.1912f)
                curveTo(14.6627f, 19.2142f, 14.4767f, 19.1932f, 14.2897f, 19.1282f)
                lineTo(10.4087f, 18.0622f)
                curveTo(9.9017f, 17.9122f, 9.5227f, 17.5512f, 9.3687f, 17.0702f)
                curveTo(9.2147f, 16.5902f, 9.3107f, 16.0742f, 9.6387f, 15.6502f)
                lineTo(11.3167f, 13.0832f)
                lineTo(9.8987f, 12.1232f)
                lineTo(7.5667f, 13.7252f)
                curveTo(7.1027f, 14.0532f, 6.8207f, 14.2472f, 6.3797f, 14.3082f)
                lineTo(6.3527f, 14.3112f)
                curveTo(5.9197f, 14.3632f, 5.4727f, 14.2382f, 5.1217f, 13.9632f)
                lineTo(3.3257f, 12.4562f)
                curveTo(2.9877f, 12.1532f, 2.7877f, 11.9172f, 2.7197f, 11.3782f)
                curveTo(2.6337f, 10.6982f, 3.1017f, 10.2392f, 3.6807f, 10.2422f)
                curveTo(4.3287f, 10.2462f, 4.3277f, 10.3442f, 4.7417f, 10.5382f)
                lineTo(6.0767f, 11.6862f)
                lineTo(8.2487f, 10.1042f)
                curveTo(8.7637f, 9.7142f, 9.1297f, 9.4752f, 9.4937f, 9.4572f)
                close()

                moveTo(13.4702f, 6.9966f)
                curveTo(13.8742f, 6.7466f, 14.3962f, 6.7416f, 14.7632f, 6.9836f)
                curveTo(15.1352f, 7.2276f, 15.3272f, 7.6756f, 15.2662f, 8.1506f)
                lineTo(14.7332f, 10.6736f)
                lineTo(10.9602f, 8.3776f)
                lineTo(13.4702f, 6.9966f)
                close()

                moveTo(7.4896f, 4.7998f)
                curveTo(8.5956f, 4.7998f, 9.4956f, 5.7008f, 9.4956f, 6.8078f)
                curveTo(9.4956f, 7.9118f, 8.5956f, 8.8108f, 7.4896f, 8.8108f)
                curveTo(6.3846f, 8.8108f, 5.4836f, 7.9118f, 5.4836f, 6.8078f)
                curveTo(5.4836f, 5.7008f, 6.3846f, 4.7998f, 7.4896f, 4.7998f)
                close()
            }
        }.build()

    val Calendar: ImageVector
        get() = ImageVector.Builder(
            name = "Calendar",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).apply {
            // Outline
            path(
                fill = SolidColor(Color.White),
                pathFillType = PathFillType.NonZero
            ) {
                 moveTo(19.5f, 8.50092f)
                 verticalLineTo(10.5009f)
                 curveTo(19.5f, 9.11892f, 18.38f, 8.00092f, 17f, 8.00092f)
                 horizontalLineTo(7f)
                 curveTo(5.62f, 8.00092f, 4.5f, 9.11892f, 4.5f, 10.5009f)
                 verticalLineTo(8.50092f)
                 curveTo(4.5f, 7.11892f, 5.62f, 6.00092f, 7f, 6.00092f)
                 horizontalLineTo(17f)
                 curveTo(18.38f, 6.00092f, 19.5f, 7.11892f, 19.5f, 8.50092f)
                 close()
            }
            // Detail
            path(
                stroke = SolidColor(Color.White),
                strokeLineWidth = 1.5f,
                strokeLineCap = StrokeCap.Round,
                strokeLineJoin = StrokeJoin.Round
            ) {
                moveTo(4.5f, 17.5f)
                curveTo(4.5f, 18.881f, 5.619f, 20f, 7f, 20f)
                horizontalLineTo(17f)
                curveTo(18.381f, 20f, 19.5f, 18.881f, 19.5f, 17.5f)
                moveTo(4.5f, 17.5f)
                verticalLineTo(8.5f)
                curveTo(4.5f, 7.119f, 5.619f, 6f, 7f, 6f)
                horizontalLineTo(17f)
                curveTo(18.381f, 6f, 19.5f, 7.119f, 19.5f, 8.5f)
                verticalLineTo(17.5f)
                moveTo(4.5f, 17.5f)
                verticalLineTo(10.5f)
                curveTo(4.5f, 9.119f, 5.619f, 8f, 7f, 8f)
                horizontalLineTo(17f)
                curveTo(18.381f, 8f, 19.5f, 9.119f, 19.5f, 10.5f)
                verticalLineTo(17.5f)
                moveTo(8.5f, 4f)
                verticalLineTo(5.5f)
                moveTo(15.5f, 4f)
                verticalLineTo(5.5f)
            }
            // Dots
            path(
                fill = SolidColor(Color.White),
                pathFillType = PathFillType.NonZero
            ) {
                moveTo(8.35009f, 14.6263f)
                curveTo(8.87109f, 14.6263f, 9.29509f, 15.0503f, 9.29509f, 15.5713f)
                curveTo(9.29509f, 16.0923f, 8.87109f, 16.5163f, 8.35009f, 16.5163f)
                curveTo(7.82909f, 16.5163f, 7.40509f, 16.0923f, 7.40509f, 15.5713f)
                curveTo(7.40509f, 15.0503f, 7.82909f, 14.6263f, 8.35009f, 14.6263f)
                close()
                moveTo(11.4133f, 14.6263f)
                curveTo(11.9343f, 14.6263f, 12.3583f, 15.0503f, 12.3583f, 15.5713f)
                curveTo(12.3583f, 16.0923f, 11.9343f, 16.5163f, 11.4133f, 16.5163f)
                curveTo(10.8923f, 16.5163f, 10.4683f, 16.0923f, 10.4683f, 15.5713f)
                curveTo(10.4683f, 15.0503f, 10.8923f, 14.6263f, 11.4133f, 14.6263f)
                close()
                moveTo(15.6494f, 11.4837f)
                curveTo(16.1704f, 11.4837f, 16.5944f, 11.9077f, 16.5944f, 12.4287f)
                curveTo(16.5944f, 12.9497f, 16.1704f, 13.3737f, 15.6494f, 13.3737f)
                curveTo(15.1284f, 13.3737f, 14.7044f, 12.9497f, 14.7044f, 12.4287f)
                curveTo(14.7044f, 11.9077f, 15.1284f, 11.4837f, 15.6494f, 11.4837f)
                close()
                moveTo(12.5857f, 11.4837f)
                curveTo(13.1077f, 11.4837f, 13.5307f, 11.9077f, 13.5307f, 12.4287f)
                curveTo(13.5307f, 12.9497f, 13.1077f, 13.3737f, 12.5857f, 13.3737f)
                curveTo(12.0647f, 13.3737f, 11.6407f, 12.9497f, 11.6407f, 12.4287f)
                curveTo(11.6407f, 11.9077f, 12.0647f, 11.4837f, 12.5857f, 11.4837f)
                close()
            }
        }.build()

    val Stop: ImageVector
        get() = ImageVector.Builder(
            name = "Stop",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).apply {
            path(
                fill = SolidColor(Color.White),
                pathFillType = PathFillType.EvenOdd
            ) {
                moveTo(16.875f, 5.125f)
                horizontalLineTo(7.125f)
                curveTo(6.025f, 5.125f, 5.125f, 6.025f, 5.125f, 7.125f)
                verticalLineTo(16.875f)
                curveTo(5.125f, 17.975f, 6.025f, 18.875f, 7.125f, 18.875f)
                horizontalLineTo(16.875f)
                curveTo(17.975f, 18.875f, 18.875f, 17.975f, 18.875f, 16.875f)
                verticalLineTo(7.125f)
                curveTo(18.875f, 6.025f, 17.975f, 5.125f, 16.875f, 5.125f)
                close()
            }
        }.build()

    val Copy: ImageVector
        get() = ImageVector.Builder(
            name = "Copy",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).apply {
            path(
                fill = SolidColor(Color.Black),
                pathFillType = PathFillType.EvenOdd
            ) {
                moveTo(14.6531f, 6.823f)
                curveTo(15.4781f, 6.823f, 16.1531f, 7.498f, 16.1531f, 8.323f)
                verticalLineTo(18.073f)
                curveTo(16.1531f, 19.448f, 15.0281f, 20.573f, 13.6531f, 20.573f)
                horizontalLineTo(6.90308f)
                curveTo(5.52808f, 20.573f, 4.40308f, 19.448f, 4.40308f, 18.073f)
                verticalLineTo(9.323f)
                curveTo(4.40308f, 7.948f, 5.52808f, 6.823f, 6.90308f, 6.823f)
                horizontalLineTo(14.6531f)
                close()
                moveTo(15.9719f, 3.427f)
                curveTo(17.9709f, 3.427f, 19.5969f, 5.053f, 19.5969f, 7.052f)
                verticalLineTo(16.117f)
                curveTo(19.5969f, 16.601f, 19.2049f, 16.992f, 18.7219f, 16.992f)
                curveTo(18.2389f, 16.992f, 17.8469f, 16.601f, 17.8469f, 16.117f)
                verticalLineTo(7.052f)
                curveTo(17.8469f, 6.018f, 17.0059f, 5.177f, 15.9719f, 5.177f)
                horizontalLineTo(8.96688f)
                curveTo(8.48288f, 5.177f, 8.09188f, 4.785f, 8.09188f, 4.302f)
                curveTo(8.09188f, 3.819f, 8.48288f, 3.427f, 8.96688f, 3.427f)
                horizontalLineTo(15.9719f)
                close()
            }
        }.build()

    val Delete: ImageVector
        get() = ImageVector.Builder(
            name = "Delete",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).apply {
            path(
                fill = SolidColor(Color.Black),
                pathFillType = PathFillType.EvenOdd
            ) {
                // Trash can lid
                moveTo(9f, 3f)
                curveTo(9f, 2.448f, 9.448f, 2f, 10f, 2f)
                horizontalLineTo(14f)
                curveTo(14.552f, 2f, 15f, 2.448f, 15f, 3f)
                verticalLineTo(4f)
                horizontalLineTo(19f)
                curveTo(19.552f, 4f, 20f, 4.448f, 20f, 5f)
                curveTo(20f, 5.552f, 19.552f, 6f, 19f, 6f)
                horizontalLineTo(5f)
                curveTo(4.448f, 6f, 4f, 5.552f, 4f, 5f)
                curveTo(4f, 4.448f, 4.448f, 4f, 5f, 4f)
                horizontalLineTo(9f)
                verticalLineTo(3f)
                close()
                // Trash can body
                moveTo(6f, 7f)
                horizontalLineTo(18f)
                lineTo(17.2f, 20.1f)
                curveTo(17.134f, 20.614f, 16.695f, 21f, 16.178f, 21f)
                horizontalLineTo(7.822f)
                curveTo(7.305f, 21f, 6.866f, 20.614f, 6.8f, 20.1f)
                lineTo(6f, 7f)
                close()
            }
        }.build()

    val EnergyScore: ImageVector
        get() = ImageVector.Builder(
            name = "EnergyScore",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).apply {
            path(fill = SolidColor(Color.Black)) {
                moveTo(7.45375f, 13.1029f)
                curveTo(5.99692f, 12.3805f, 5.10809f, 11.1482f, 4.16981f, 10.9136f)
                curveTo(3.8386f, 10.8308f, 3.43636f, 10.8751f, 3.23153f, 11.1482f)
                curveTo(3.02672f, 11.4213f, 3.03578f, 11.885f, 3.23154f, 12.1646f)
                curveTo(3.77885f, 12.9465f, 5.0526f, 13.9737f, 6.51548f, 14.9794f)
                curveTo(7.60844f, 15.7308f, 9.38913f, 16.6167f, 9.79396f, 16.8152f)
                curveTo(9.84595f, 16.8407f, 9.87899f, 16.8921f, 9.88117f, 16.95f)
                lineTo(10.0283f, 20.8495f)
                curveTo(10.0315f, 20.9335f, 10.1005f, 21f, 10.1846f, 21f)
                horizontalLineTo(15.0436f)
                curveTo(15.1278f, 21f, 15.1969f, 20.9333f, 15.1999f, 20.8492f)
                lineTo(15.3477f, 16.7099f)
                curveTo(15.3497f, 16.655f, 15.3801f, 16.6052f, 15.4281f, 16.5785f)
                curveTo(15.772f, 16.3868f, 17.1871f, 15.591f, 18.0875f, 14.9794f)
                curveTo(19.6142f, 13.9425f, 21.2151f, 13.1029f, 21.7624f, 12.0082f)
                curveTo(21.9399f, 11.6532f, 21.9997f, 11.31f, 21.7624f, 10.9918f)
                curveTo(21.501f, 10.6413f, 20.9717f, 10.7013f, 20.5896f, 10.9136f)
                curveTo(19.8859f, 11.3047f, 18.664f, 12.5526f, 17.2274f, 13.2593f)
                curveTo(15.5188f, 14.0998f, 13.0878f, 14.5834f, 12.6186f, 14.5834f)
                curveTo(12.1867f, 14.5867f, 9.30632f, 14.0215f, 7.45375f, 13.1029f)
                close()
            }
            path(fill = SolidColor(Color.Black)) {
                moveTo(14.8817f, 11.4609f)
                curveTo(14.8817f, 12.7132f, 13.8665f, 13.7284f, 12.6142f, 13.7284f)
                curveTo(11.3619f, 13.7284f, 10.3468f, 12.7132f, 10.3468f, 11.4609f)
                curveTo(10.3468f, 10.2086f, 11.3619f, 9.19342f, 12.6142f, 9.19342f)
                curveTo(13.8665f, 9.19342f, 14.8817f, 10.2086f, 14.8817f, 11.4609f)
                close()
            }
            path(fill = SolidColor(Color.Black)) {
                moveTo(11.7542f, 2.86008f)
                curveTo(11.7542f, 2.38507f, 12.1392f, 2f, 12.6142f, 2f)
                curveTo(13.0893f, 2f, 13.4743f, 2.38507f, 13.4743f, 2.86008f)
                verticalLineTo(4.26749f)
                curveTo(13.4743f, 4.7425f, 13.0893f, 5.12757f, 12.6142f, 5.12757f)
                curveTo(12.1392f, 5.12757f, 11.7542f, 4.7425f, 11.7542f, 4.26749f)
                verticalLineTo(2.86008f)
                close()
            }
            path(fill = SolidColor(Color.Black)) {
                moveTo(16.4851f, 3.70282f)
                curveTo(16.821f, 3.36694f, 17.3655f, 3.36694f, 17.7014f, 3.70282f)
                curveTo(18.0373f, 4.0387f, 18.0373f, 4.58328f, 17.7014f, 4.91916f)
                lineTo(16.7062f, 5.91435f)
                curveTo(16.3704f, 6.25023f, 15.8258f, 6.25023f, 15.4899f, 5.91435f)
                curveTo(15.154f, 5.57847f, 15.154f, 5.03389f, 15.4899f, 4.69801f)
                lineTo(16.4851f, 3.70282f)
                close()
            }
            path(fill = SolidColor(Color.Black)) {
                moveTo(8.59695f, 3.70282f)
                curveTo(8.26107f, 3.36694f, 7.7165f, 3.36694f, 7.38061f, 3.70282f)
                curveTo(7.04473f, 4.0387f, 7.04473f, 4.58328f, 7.38061f, 4.91916f)
                lineTo(8.3758f, 5.91435f)
                curveTo(8.71168f, 6.25023f, 9.25626f, 6.25023f, 9.59214f, 5.91435f)
                curveTo(9.92803f, 5.57847f, 9.92803f, 5.03389f, 9.59214f, 4.69801f)
                lineTo(8.59695f, 3.70282f)
                close()
            }
            path(fill = SolidColor(Color.Black)) {
                moveTo(19.0889f, 7.3374f)
                curveTo(19.5478f, 7.21446f, 20.0194f, 7.48675f, 20.1423f, 7.94557f)
                curveTo(20.2653f, 8.4044f, 19.993f, 8.87601f, 19.5342f, 8.99895f)
                lineTo(18.1747f, 9.36322f)
                curveTo(17.7159f, 9.48616f, 17.2443f, 9.21387f, 17.1213f, 8.75505f)
                curveTo(16.9984f, 8.29622f, 17.2707f, 7.82461f, 17.7295f, 7.70167f)
                lineTo(19.0889f, 7.3374f)
                close()
            }
            path(fill = SolidColor(Color.Black)) {
                moveTo(5.9931f, 7.3374f)
                curveTo(5.53427f, 7.21446f, 5.06266f, 7.48675f, 4.93972f, 7.94557f)
                curveTo(4.81678f, 8.4044f, 5.08906f, 8.87601f, 5.54789f, 8.99895f)
                lineTo(6.90734f, 9.36322f)
                curveTo(7.36616f, 9.48616f, 7.83778f, 9.21387f, 7.96072f, 8.75505f)
                curveTo(8.08366f, 8.29622f, 7.81138f, 7.82461f, 7.35255f, 7.70167f)
                lineTo(5.9931f, 7.3374f)
                close()
            }
        }.build()
    val WeightScale: ImageVector
        get() = ImageVector.Builder(
            name = "WeightScale",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).apply {
            path(
                fill = SolidColor(Color.White),
                pathFillType = PathFillType.EvenOdd
            ) {
                moveTo(17.5998f, 19.2f)
                horizontalLineTo(6.40019f)
                curveTo(5.51639f, 19.2f, 4.79999f, 18.4836f, 4.79999f, 17.5998f)
                verticalLineTo(6.40025f)
                curveTo(4.79999f, 5.51645f, 5.51639f, 4.80005f, 6.40019f, 4.80005f)
                horizontalLineTo(17.5998f)
                curveTo(18.4836f, 4.80005f, 19.2f, 5.51645f, 19.2f, 6.40025f)
                verticalLineTo(17.5998f)
                curveTo(19.2f, 18.4836f, 18.4836f, 19.2f, 17.5998f, 19.2f)
                close()
            }
            path(
                fill = SolidColor(Color.White),
                pathFillType = PathFillType.NonZero
            ) {
                moveTo(9.936f, 6f)
                horizontalLineTo(14.064f)
                curveTo(14.4702f, 6f, 14.8002f, 6.3384f, 14.8002f, 6.7554f)
                verticalLineTo(12.0444f)
                curveTo(14.8002f, 12.462f, 14.4702f, 12.7998f, 14.064f, 12.7998f)
                horizontalLineTo(9.936f)
                curveTo(9.5298f, 12.7998f, 9.1998f, 12.462f, 9.1998f, 12.0444f)
                verticalLineTo(6.7554f)
                curveTo(9.1998f, 6.3384f, 9.5298f, 6f, 9.936f, 6f)
                close()
            }
            path(
                fill = SolidColor(Color.White),
                pathFillType = PathFillType.EvenOdd
            ) {
                moveTo(11.1054f, 6.59399f)
                horizontalLineTo(10.5102f)
                curveTo(10.1232f, 6.59399f, 9.80759f, 6.91019f, 9.80759f, 7.29839f)
                verticalLineTo(9.08939f)
                curveTo(9.80759f, 9.47699f, 10.1232f, 9.79379f, 10.5102f, 9.79379f)
                horizontalLineTo(11.1054f)
                curveTo(11.4924f, 9.79379f, 11.808f, 9.47699f, 11.808f, 9.08939f)
                verticalLineTo(7.29839f)
                curveTo(11.808f, 6.91019f, 11.4924f, 6.59399f, 11.1054f, 6.59399f)
                close()
                moveTo(11.1054f, 7.19399f)
                curveTo(11.1606f, 7.19399f, 11.208f, 7.24199f, 11.208f, 7.29839f)
                verticalLineTo(9.08939f)
                curveTo(11.208f, 9.14579f, 11.1606f, 9.19379f, 11.1054f, 9.19379f)
                horizontalLineTo(10.5102f)
                curveTo(10.4544f, 9.19379f, 10.4076f, 9.14579f, 10.4076f, 9.08939f)
                verticalLineTo(7.29839f)
                curveTo(10.4076f, 7.24199f, 10.4544f, 7.19399f, 10.5102f, 7.19399f)
                horizontalLineTo(11.1054f)
                close()
                moveTo(13.4898f, 6.59399f)
                horizontalLineTo(12.8946f)
                curveTo(12.5076f, 6.59399f, 12.1926f, 6.91019f, 12.1926f, 7.29839f)
                verticalLineTo(9.08939f)
                curveTo(12.1926f, 9.47699f, 12.5076f, 9.79379f, 12.8946f, 9.79379f)
                horizontalLineTo(13.4898f)
                curveTo(13.8768f, 9.79379f, 14.1924f, 9.47699f, 14.1924f, 9.08939f)
                verticalLineTo(7.29839f)
                curveTo(14.1924f, 6.91019f, 13.8768f, 6.59399f, 13.4898f, 6.59399f)
                close()
                moveTo(13.4898f, 7.19399f)
                curveTo(13.5456f, 7.19399f, 13.5924f, 7.24199f, 13.5924f, 7.29839f)
                verticalLineTo(9.08939f)
                curveTo(13.5924f, 9.14579f, 13.5456f, 9.19379f, 13.4898f, 9.19379f)
                horizontalLineTo(12.8946f)
                curveTo(12.8394f, 9.19379f, 12.7926f, 9.14579f, 12.7926f, 9.08939f)
                verticalLineTo(7.29839f)
                curveTo(12.7926f, 7.24199f, 12.8394f, 7.19399f, 12.8946f, 7.19399f)
                horizontalLineTo(13.4898f)
                close()
            }
        }.build()

    val BodyComposition: ImageVector
        get() = ImageVector.Builder(
            name = "BodyComposition",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).apply {
            path(
                fill = SolidColor(Color.White),
                pathFillType = PathFillType.NonZero
            ) {
                moveTo(14.981f, 12.6635f)
                lineTo(13.8897f, 10.111f)
                curveTo(13.6885f, 9.60912f, 13.373f, 9.427f, 13.373f, 9.427f)
                curveTo(13.3003f, 9.38465f, 13.1704f, 9.34936f, 13.0857f, 9.34936f)
                horizontalLineTo(10.9144f)
                curveTo(10.8297f, 9.34936f, 10.6998f, 9.38465f, 10.6271f, 9.427f)
                curveTo(10.6271f, 9.427f, 10.3116f, 9.60841f, 10.1104f, 10.111f)
                lineTo(9.01909f, 12.6635f)
                curveTo(8.98097f, 12.7616f, 8.85532f, 13.2868f, 9.41579f, 13.351f)
                curveTo(9.74332f, 13.3891f, 9.93179f, 13.1428f, 10.044f, 12.895f)
                curveTo(10.0476f, 12.8872f, 10.0539f, 12.8816f, 10.0567f, 12.8738f)
                lineTo(10.9264f, 10.8917f)
                curveTo(10.9574f, 10.8112f, 10.9709f, 10.8155f, 10.9589f, 10.8995f)
                lineTo(10.0066f, 16.4336f)
                curveTo(9.96356f, 16.5889f, 9.96356f, 16.6898f, 9.96356f, 16.8091f)
                curveTo(9.96356f, 17.1649f, 10.2247f, 17.4028f, 10.5763f, 17.4028f)
                curveTo(10.86f, 17.4028f, 11.1141f, 17.1952f, 11.1918f, 16.8776f)
                curveTo(11.1918f, 16.8776f, 11.748f, 13.495f, 11.7487f, 13.4915f)
                curveTo(11.7509f, 13.4886f, 11.7523f, 13.4837f, 11.7523f, 13.4816f)
                curveTo(11.7523f, 13.4816f, 11.8617f, 13.4816f, 11.9993f, 13.4809f)
                curveTo(12.137f, 13.4816f, 12.2464f, 13.4816f, 12.2464f, 13.4816f)
                curveTo(12.2471f, 13.4837f, 12.2485f, 13.4886f, 12.2499f, 13.4915f)
                curveTo(12.2513f, 13.495f, 12.8076f, 16.8776f, 12.8076f, 16.8776f)
                curveTo(12.8852f, 17.1952f, 13.1393f, 17.4028f, 13.4231f, 17.4028f)
                curveTo(13.7746f, 17.4028f, 14.0344f, 17.1649f, 14.0344f, 16.8091f)
                curveTo(14.0344f, 16.6898f, 14.0351f, 16.5889f, 13.9927f, 16.4336f)
                lineTo(13.0405f, 10.8995f)
                curveTo(13.0278f, 10.8155f, 13.0419f, 10.8112f, 13.073f, 10.8917f)
                lineTo(13.9426f, 12.8738f)
                curveTo(13.9454f, 12.8816f, 13.9525f, 12.8872f, 13.9553f, 12.895f)
                curveTo(14.0676f, 13.1428f, 14.256f, 13.3884f, 14.5843f, 13.351f)
                curveTo(15.1447f, 13.2868f, 15.0191f, 12.7616f, 14.981f, 12.6635f)
                close()
                moveTo(12f, 8.80512f)
                curveTo(12.5986f, 8.80512f, 13.0836f, 8.32018f, 13.0836f, 7.72159f)
                curveTo(13.0836f, 7.123f, 12.5986f, 6.63806f, 12f, 6.63806f)
                curveTo(11.4014f, 6.63806f, 10.9165f, 7.123f, 10.9165f, 7.72159f)
                curveTo(10.9165f, 8.32018f, 11.4014f, 8.80512f, 12f, 8.80512f)
                close()
            }
        }.build()

    val Height: ImageVector
        get() = ImageVector.Builder(
            name = "Height",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).apply {
            path(
                fill = SolidColor(Color.White),
                pathFillType = PathFillType.EvenOdd
            ) {
                moveTo(17.0759f, 21f)
                curveTo(16.4829f, 21f, 15.9789f, 20.567f, 15.8769f, 19.97f)
                lineTo(15.0509f, 14.862f)
                curveTo(15.0259f, 14.712f, 14.8989f, 14.7f, 14.8599f, 14.7f)
                curveTo(14.8219f, 14.7f, 14.6949f, 14.712f, 14.6689f, 14.862f)
                lineTo(13.8439f, 19.97f)
                curveTo(13.7419f, 20.567f, 13.2379f, 21f, 12.6449f, 21f)
                curveTo(12.5969f, 21f, 12.5469f, 20.997f, 12.4979f, 20.99f)
                curveTo(12.1779f, 20.951f, 11.8929f, 20.782f, 11.6919f, 20.513f)
                curveTo(11.4849f, 20.233f, 11.3959f, 19.874f, 11.4489f, 19.525f)
                lineTo(12.7939f, 11.029f)
                lineTo(11.7249f, 14.217f)
                curveTo(11.5569f, 14.604f, 11.1799f, 14.854f, 10.7659f, 14.854f)
                curveTo(10.6729f, 14.854f, 10.5779f, 14.842f, 10.4859f, 14.815f)
                curveTo(10.1919f, 14.734f, 9.9569f, 14.537f, 9.8259f, 14.257f)
                curveTo(9.6899f, 13.967f, 9.6859f, 13.627f, 9.8149f, 13.324f)
                lineTo(11.2089f, 9.377f)
                curveTo(11.2939f, 9.15f, 11.5159f, 8.698f, 12.0029f, 8.371f)
                curveTo(12.4239f, 8.091f, 12.8439f, 8.029f, 13.1239f, 8.029f)
                horizontalLineTo(16.5959f)
                curveTo(16.8759f, 8.029f, 17.2969f, 8.091f, 17.7179f, 8.371f)
                curveTo(18.2049f, 8.698f, 18.4269f, 9.15f, 18.5169f, 9.389f)
                lineTo(19.9059f, 13.324f)
                curveTo(20.0349f, 13.627f, 20.0309f, 13.967f, 19.8949f, 14.257f)
                curveTo(19.7639f, 14.537f, 19.5289f, 14.734f, 19.2349f, 14.815f)
                curveTo(19.1429f, 14.842f, 19.0489f, 14.854f, 18.9539f, 14.854f)
                curveTo(18.5399f, 14.854f, 18.1639f, 14.604f, 17.9949f, 14.217f)
                lineTo(16.9269f, 11.029f)
                lineTo(18.2719f, 19.525f)
                curveTo(18.3249f, 19.874f, 18.2359f, 20.233f, 18.0279f, 20.513f)
                curveTo(17.8279f, 20.782f, 17.5429f, 20.951f, 17.2229f, 20.99f)
                curveTo(17.1739f, 20.997f, 17.1239f, 21f, 17.0759f, 21f)
                close()
                moveTo(16.7989f, 4.985f)
                curveTo(16.7989f, 6.081f, 15.9319f, 6.971f, 14.8639f, 6.971f)
                curveTo(13.7949f, 6.971f, 12.9279f, 6.081f, 12.9279f, 4.985f)
                curveTo(12.9279f, 3.888f, 13.7949f, 3f, 14.8639f, 3f)
                curveTo(15.9319f, 3f, 16.7989f, 3.888f, 16.7989f, 4.985f)
                close()
            }
            path(
                stroke = SolidColor(Color.White),
                strokeLineWidth = 1.5f,
                strokeLineCap = StrokeCap.Round,
                strokeLineJoin = StrokeJoin.Round
            ) {
                moveTo(4f, 3.79401f)
                horizontalLineTo(7.613f)
                moveTo(5.806f, 3.79401f)
                verticalLineTo(20.206f)
                moveTo(4f, 20.206f)
                horizontalLineTo(7.613f)
            }
        }.build()

    val AI: ImageVector
        get() = ImageVector.Builder(
            name = "AI",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).apply {
            path(
                fill = SolidColor(Color.White),
                pathFillType = PathFillType.EvenOdd
            ) {
                moveTo(7.78192f, 4.4269f)
                lineTo(8.34692f, 2.9999f)
                curveTo(8.41492f, 2.8669f, 8.60692f, 2.8669f, 8.67492f, 2.9999f)
                lineTo(9.23992f, 4.4269f)
                lineTo(10.3549f, 4.9889f)
                curveTo(10.4899f, 5.0559f, 10.4899f, 5.2469f, 10.3549f, 5.3149f)
                lineTo(9.23992f, 5.8769f)
                lineTo(8.67492f, 7.3039f)
                curveTo(8.60692f, 7.4369f, 8.41492f, 7.4369f, 8.34692f, 7.3039f)
                lineTo(7.78192f, 5.8769f)
                lineTo(6.66692f, 5.3149f)
                curveTo(6.53192f, 5.2469f, 6.53192f, 5.0559f, 6.66692f, 4.9889f)
                lineTo(7.78192f, 4.4269f)
                close()
            }
            path(
                fill = SolidColor(Color.White),
                pathFillType = PathFillType.EvenOdd
            ) {
                moveTo(17.6091f, 18.0419f)
                lineTo(18.1741f, 16.6139f)
                curveTo(18.2421f, 16.4809f, 18.4341f, 16.4809f, 18.5021f, 16.6139f)
                lineTo(19.0671f, 18.0419f)
                lineTo(20.1821f, 18.6029f)
                curveTo(20.3161f, 18.6709f, 20.3161f, 18.8619f, 20.1821f, 18.9289f)
                lineTo(19.0671f, 19.4909f)
                lineTo(18.5021f, 20.9179f)
                curveTo(18.4341f, 21.0519f, 18.2421f, 21.0519f, 18.1741f, 20.9179f)
                lineTo(17.6091f, 19.4909f)
                lineTo(16.4941f, 18.9289f)
                curveTo(16.3591f, 18.8619f, 16.3591f, 18.6709f, 16.4941f, 18.6029f)
                lineTo(17.6091f, 18.0419f)
                close()
            }
            path(
                fill = SolidColor(Color.White),
                pathFillType = PathFillType.EvenOdd
            ) {
                moveTo(7.64596f, 8.75803f)
                curveTo(7.79696f, 8.45403f, 8.22296f, 8.45403f, 8.37396f, 8.75803f)
                lineTo(8.88796f, 10.092f)
                lineTo(8.47696f, 10.302f)
                curveTo(7.04496f, 11.054f, 7.31796f, 12.454f, 8.44496f, 12.95f)
                lineTo(8.47696f, 12.967f)
                lineTo(11.247f, 14.372f)
                lineTo(11.274f, 14.442f)
                lineTo(9.62596f, 15.295f)
                lineTo(8.37396f, 18.538f)
                curveTo(8.22296f, 18.842f, 7.79696f, 18.842f, 7.64596f, 18.538f)
                lineTo(6.39396f, 15.295f)
                lineTo(3.92396f, 14.019f)
                curveTo(3.62496f, 13.865f, 3.62496f, 13.431f, 3.92396f, 13.277f)
                lineTo(6.39396f, 12.001f)
                lineTo(7.64596f, 8.75803f)
                close()
            }
            path(
                fill = SolidColor(Color.White),
                pathFillType = PathFillType.EvenOdd
            ) {
                moveTo(16.3279f, 9.46197f)
                lineTo(14.6339f, 5.14797f)
                curveTo(14.4289f, 4.74397f, 13.8529f, 4.74397f, 13.6479f, 5.14797f)
                lineTo(11.9529f, 9.46197f)
                lineTo(8.60891f, 11.159f)
                curveTo(8.20491f, 11.363f, 8.20491f, 11.94f, 8.60891f, 12.145f)
                lineTo(11.9529f, 13.842f)
                lineTo(13.6479f, 18.156f)
                curveTo(13.8529f, 18.56f, 14.4289f, 18.56f, 14.6339f, 18.156f)
                lineTo(16.3279f, 13.842f)
                lineTo(19.6729f, 12.145f)
                curveTo(20.0759f, 11.94f, 20.0759f, 11.363f, 19.6729f, 11.159f)
                lineTo(16.3279f, 9.46197f)
                close()
            }
        }.build()

    val Gym: ImageVector
        get() = ImageVector.Builder(
            name = "Gym",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).apply {
            path(
                fill = SolidColor(Color.White),
                pathFillType = PathFillType.EvenOdd
            ) {
                moveTo(11.254f, 9.67699f)
                lineTo(21.337f, 16.362f)
                curveTo(21.687f, 16.551f, 21.925f, 16.898f, 21.972f, 17.291f)
                curveTo(22.053f, 17.96f, 21.575f, 18.57f, 20.907f, 18.65f)
                curveTo(20.636f, 18.684f, 20.369f, 18.627f, 20.135f, 18.485f)
                lineTo(14.079f, 14.487f)
                lineTo(13.105f, 16.104f)
                lineTo(15.775f, 16.819f)
                curveTo(16.223f, 16.967f, 16.549f, 17.365f, 16.606f, 17.833f)
                curveTo(16.686f, 18.501f, 16.207f, 19.11f, 15.539f, 19.191f)
                curveTo(15.353f, 19.214f, 15.167f, 19.193f, 14.98f, 19.128f)
                lineTo(11.099f, 18.062f)
                curveTo(10.592f, 17.912f, 10.213f, 17.551f, 10.059f, 17.07f)
                curveTo(9.90501f, 16.59f, 10.001f, 16.074f, 10.329f, 15.65f)
                lineTo(12.007f, 13.083f)
                lineTo(10.589f, 12.123f)
                lineTo(8.25701f, 13.725f)
                curveTo(7.79301f, 14.053f, 7.51101f, 14.247f, 7.07001f, 14.308f)
                lineTo(7.04301f, 14.311f)
                curveTo(6.61001f, 14.363f, 6.16301f, 14.238f, 5.81201f, 13.963f)
                lineTo(4.01601f, 12.456f)
                curveTo(3.67801f, 12.153f, 3.47801f, 11.917f, 3.41001f, 11.378f)
                curveTo(3.32401f, 10.698f, 3.79201f, 10.239f, 4.37101f, 10.242f)
                curveTo(5.01901f, 10.246f, 5.01801f, 10.344f, 5.43201f, 10.538f)
                lineTo(6.76701f, 11.686f)
                lineTo(8.93901f, 10.104f)
                curveTo(9.45401f, 9.71399f, 9.82001f, 9.47499f, 10.184f, 9.45699f)
                curveTo(10.561f, 9.43899f, 10.94f, 9.49099f, 11.254f, 9.67699f)
                close()
                moveTo(15.454f, 6.98399f)
                curveTo(15.826f, 7.22799f, 16.018f, 7.67599f, 15.957f, 8.15099f)
                lineTo(15.424f, 10.674f)
                lineTo(11.651f, 8.37799f)
                lineTo(14.161f, 6.99699f)
                curveTo(14.565f, 6.74699f, 15.087f, 6.74199f, 15.454f, 6.98399f)
                close()
                moveTo(8.18001f, 4.79999f)
                curveTo(9.28601f, 4.79999f, 10.186f, 5.70099f, 10.186f, 6.80799f)
                curveTo(10.186f, 7.91199f, 9.28601f, 8.81099f, 8.18001f, 8.81099f)
                curveTo(7.07501f, 8.81099f, 6.17401f, 7.91199f, 6.17401f, 6.80799f)
                curveTo(6.17401f, 5.70099f, 7.07501f, 4.79999f, 8.18001f, 4.79999f)
                close()
            }
        }.build()
    val DeleteOutline: ImageVector
        get() = ImageVector.Builder(
            name = "DeleteOutline",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).apply {
            path(
                stroke = SolidColor(Color.White),
                strokeLineWidth = 1.5f,
                strokeLineCap = StrokeCap.Round,
                strokeLineJoin = StrokeJoin.Round
            ) {
                moveTo(18.2369f, 6.89562f)
                lineTo(17.2849f, 19.1546f)
                curveTo(17.2049f, 20.1966f, 16.3359f, 20.9996f, 15.2919f, 20.9996f)
                horizontalLineTo(8.70893f)
                curveTo(7.66393f, 20.9996f, 6.79593f, 20.1966f, 6.71493f, 19.1546f)
                lineTo(5.76393f, 6.89562f)
                moveTo(4.31403f, 6.50012f)
                horizontalLineTo(19.686f)
                moveTo(13.8702f, 11.6642f)
                verticalLineTo(16.1142f)
                moveTo(10.1304f, 11.6642f)
                verticalLineTo(16.1142f)
                moveTo(9.24963f, 6.00012f)
                verticalLineTo(3.75012f)
                curveTo(9.24963f, 3.47512f, 9.47463f, 3.25012f, 9.74963f, 3.25012f)
                horizontalLineTo(14.2496f)
                curveTo(14.5246f, 3.25012f, 14.7496f, 3.47512f, 14.7496f, 3.75012f)
                verticalLineTo(6.00012f)
            }
        }.build()

    val ShareOutline: ImageVector
        get() = ImageVector.Builder(
            name = "ShareOutline",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).apply {
            path(
                fill = SolidColor(Color.White),
                pathFillType = PathFillType.EvenOdd
            ) {
                moveTo(16.3221f, 3.12512f)
                curveTo(18.0065f, 3.12512f, 19.372f, 4.49102f, 19.372f, 6.17612f)
                curveTo(19.372f, 7.85952f, 18.0061f, 9.22502f, 16.3221f, 9.22502f)
                curveTo(15.4964f, 9.22502f, 14.7473f, 8.89722f, 14.198f, 8.36462f)
                lineTo(9.58913f, 11.0879f)
                curveTo(9.67933f, 11.3758f, 9.72803f, 11.6822f, 9.72803f, 11.9998f)
                curveTo(9.72803f, 12.3175f, 9.67933f, 12.624f, 9.58913f, 12.9122f)
                lineTo(14.1979f, 15.6356f)
                curveTo(14.7472f, 15.1029f, 15.4963f, 14.775f, 16.3221f, 14.775f)
                curveTo(18.0063f, 14.775f, 19.372f, 16.1407f, 19.372f, 17.8249f)
                curveTo(19.372f, 19.5099f, 18.0066f, 20.875f, 16.3221f, 20.875f)
                curveTo(14.6367f, 20.875f, 13.2711f, 19.5101f, 13.2711f, 17.8249f)
                curveTo(13.2711f, 17.5071f, 13.3197f, 17.2006f, 13.41f, 16.9124f)
                lineTo(8.80203f, 14.1894f)
                curveTo(8.25273f, 14.723f, 7.50363f, 15.0518f, 6.67793f, 15.0518f)
                curveTo(4.99393f, 15.0518f, 3.62793f, 13.6841f, 3.62793f, 11.9998f)
                curveTo(3.62793f, 10.3162f, 4.99403f, 8.94972f, 6.67793f, 8.94972f)
                curveTo(7.50353f, 8.94972f, 8.25273f, 9.2782f, 8.80193f, 9.81142f)
                lineTo(13.4099f, 7.08792f)
                curveTo(13.3197f, 6.80002f, 13.2711f, 6.49372f, 13.2711f, 6.17612f)
                curveTo(13.2711f, 4.49082f, 14.6368f, 3.12512f, 16.3221f, 3.12512f)
                close()
                moveTo(16.3221f, 16.275f)
                curveTo(15.4654f, 16.275f, 14.7711f, 16.9691f, 14.7711f, 17.8249f)
                curveTo(14.7711f, 18.6814f, 15.465f, 19.375f, 16.3221f, 19.375f)
                curveTo(17.1782f, 19.375f, 17.872f, 18.6813f, 17.872f, 17.8249f)
                curveTo(17.872f, 16.9692f, 17.1778f, 16.275f, 16.3221f, 16.275f)
                close()
                moveTo(6.67793f, 10.4497f)
                curveTo(5.82263f, 10.4497f, 5.12803f, 11.1446f, 5.12803f, 11.9998f)
                curveTo(5.12803f, 12.8561f, 5.82273f, 13.5518f, 6.67793f, 13.5518f)
                curveTo(7.53323f, 13.5518f, 8.22803f, 12.8561f, 8.22803f, 11.9998f)
                curveTo(8.22803f, 11.1446f, 7.53333f, 10.4497f, 6.67793f, 10.4497f)
                close()
                moveTo(16.3221f, 4.62512f)
                curveTo(15.4653f, 4.62512f, 14.7711f, 5.31932f, 14.7711f, 6.17612f)
                curveTo(14.7711f, 7.03102f, 15.4655f, 7.72502f, 16.3221f, 7.72502f)
                curveTo(17.1777f, 7.72502f, 17.872f, 7.03092f, 17.872f, 6.17612f)
                curveTo(17.872f, 5.31942f, 17.1779f, 4.62512f, 16.3221f, 4.62512f)
                close()
            }
        }.build()

    val Add: ImageVector
        get() = ImageVector.Builder(
            name = "Add",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).apply {
            path(
                fill = SolidColor(Color.White),
                pathFillType = PathFillType.NonZero
            ) {
                moveTo(20.375f, 11.25f)
                horizontalLineTo(12.75f)
                verticalLineTo(3.625f)
                curveTo(12.75f, 3.211f, 12.414f, 2.875f, 12f, 2.875f)
                curveTo(11.586f, 2.875f, 11.25f, 3.211f, 11.25f, 3.625f)
                verticalLineTo(11.25f)
                horizontalLineTo(3.625f)
                curveTo(3.211f, 11.25f, 2.875f, 11.586f, 2.875f, 12f)
                curveTo(2.875f, 12.414f, 3.211f, 12.75f, 3.625f, 12.75f)
                horizontalLineTo(11.25f)
                verticalLineTo(20.375f)
                curveTo(11.25f, 20.789f, 11.586f, 21.125f, 12f, 21.125f)
                curveTo(12.414f, 21.125f, 12.75f, 20.789f, 12.75f, 20.375f)
                verticalLineTo(12.75f)
                horizontalLineTo(20.375f)
                curveTo(20.789f, 12.75f, 21.125f, 12.414f, 21.125f, 12f)
                curveTo(21.125f, 11.586f, 20.789f, 11.25f, 20.375f, 11.25f)
                close()
            }
        }.build()

    val CopyOutline: ImageVector
        get() = ImageVector.Builder(
            name = "CopyOutline",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).apply {
            path(
                stroke = SolidColor(Color.White),
                strokeLineWidth = 1.5f,
                strokeLineCap = StrokeCap.Round,
                strokeLineJoin = StrokeJoin.Round
            ) {
                moveTo(18.881f, 16.216f)
                verticalLineTo(6.95999f)
                curveTo(18.881f, 5.44199f, 17.65f, 4.20999f, 16.131f, 4.20999f)
                horizontalLineTo(9.06599f)
                moveTo(14.11f, 7.52949f)
                horizontalLineTo(7.10999f)
                curveTo(6.00999f, 7.52949f, 5.10999f, 8.42949f, 5.10999f, 9.52949f)
                verticalLineTo(17.7795f)
                curveTo(5.10999f, 18.8805f, 6.00999f, 19.7795f, 7.10999f, 19.7795f)
                horizontalLineTo(13.61f)
                curveTo(14.71f, 19.7795f, 15.61f, 18.8805f, 15.61f, 17.7795f)
                verticalLineTo(9.02949f)
                curveTo(15.61f, 8.20449f, 14.935f, 7.52949f, 14.11f, 7.52949f)
                close()
            }
        }.build()

    val EditOutline: ImageVector
        get() = ImageVector.Builder(
            name = "EditOutline",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).apply {
            path(
                stroke = SolidColor(Color.White),
                strokeLineWidth = 1.5f,
                strokeLineCap = StrokeCap.Round,
                strokeLineJoin = StrokeJoin.Round
            ) {
                moveTo(11.0217f, 18.4762f)
                curveTo(10.8297f, 18.6682f, 10.0967f, 19.2232f, 9.82471f, 19.2772f)
                lineTo(4.6567f, 20.2902f)
                curveTo(4.3847f, 20.3442f, 4.2067f, 20.1652f, 4.2597f, 19.8932f)
                lineTo(5.2737f, 14.7282f)
                curveTo(5.3277f, 14.4562f, 5.8817f, 13.7222f, 6.0737f, 13.5302f)
                moveTo(11.2102f, 18.2926f)
                curveTo(10.9182f, 18.5846f, 10.4412f, 18.5846f, 10.1492f, 18.2926f)
                lineTo(6.2602f, 14.4036f)
                curveTo(5.9692f, 14.1116f, 5.9692f, 13.6346f, 6.2602f, 13.3426f)
                lineTo(14.7672f, 4.83563f)
                curveTo(15.5482f, 4.05463f, 16.8142f, 4.05463f, 17.5962f, 4.83563f)
                lineTo(19.7172f, 6.95763f)
                curveTo(20.4982f, 7.73763f, 20.4982f, 9.00363f, 19.7172f, 9.78563f)
                lineTo(11.2102f, 18.2926f)
                close()
            }
        }.build()
    val More: ImageVector
        get() = ImageVector.Builder(
            name = "More",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).apply {
            path(
                fill = SolidColor(Color.White),
                pathFillType = PathFillType.EvenOdd
            ) {
                moveTo(11.6f, 16.75f)
                curveTo(12.4684f, 16.75f, 13.1f, 17.3816f, 13.1f, 18.25f)
                curveTo(13.1f, 19.1184f, 12.4684f, 19.75f, 11.6f, 19.75f)
                curveTo(10.8105f, 19.75f, 10.1f, 19.1184f, 10.1f, 18.25f)
                curveTo(10.1f, 17.3816f, 10.7315f, 16.75f, 11.6f, 16.75f)
                close()
                moveTo(11.6f, 10.5f)
                curveTo(12.4684f, 10.5f, 13.1f, 11.1316f, 13.1f, 12f)
                curveTo(13.1f, 12.8684f, 12.4684f, 13.5f, 11.6f, 13.5f)
                curveTo(10.8105f, 13.5f, 10.1f, 12.8684f, 10.1f, 12f)
                curveTo(10.1f, 11.1316f, 10.7315f, 10.5f, 11.6f, 10.5f)
                close()
                moveTo(11.6f, 4.25f)
                curveTo(12.3894f, 4.25f, 13.1f, 4.88157f, 13.1f, 5.75f)
                curveTo(13.1f, 6.61843f, 12.4684f, 7.25f, 11.6f, 7.25f)
                curveTo(10.8105f, 7.25f, 10.1f, 6.61843f, 10.1f, 5.75f)
                curveTo(10.1f, 4.88157f, 10.7315f, 4.25f, 11.6f, 4.25f)
                close()
            }
        }.build()

    val Star: ImageVector
        get() = ImageVector.Builder(
            name = "Star",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).apply {
            path(
                fill = SolidColor(Color.White),
                pathFillType = PathFillType.EvenOdd
            ) {
                moveTo(15.6055f, 8.71812f)
                curveTo(15.2515f, 8.71812f, 14.9375f, 8.49012f, 14.8285f, 8.15412f)
                lineTo(12.7775f, 4.06512f)
                curveTo(12.5335f, 3.31212f, 11.4675f, 3.31212f, 11.2225f, 4.06512f)
                lineTo(9.17153f, 8.15412f)
                curveTo(9.06253f, 8.49012f, 8.74953f, 8.71812f, 8.39453f, 8.71812f)
                lineTo(4.06753f, 9.53512f)
                curveTo(3.27553f, 9.53512f, 2.94653f, 10.5491f, 3.58653f, 11.0141f)
                lineTo(6.71053f, 14.1511f)
                curveTo(6.99853f, 14.3591f, 7.11753f, 14.7281f, 7.00753f, 15.0651f)
                lineTo(6.32053f, 19.4261f)
                curveTo(6.07553f, 20.1791f, 6.93753f, 20.8051f, 7.57853f, 20.3401f)
                lineTo(11.5195f, 18.0211f)
                curveTo(11.8055f, 17.8131f, 12.1945f, 17.8131f, 12.4805f, 18.0211f)
                lineTo(16.4215f, 20.3401f)
                curveTo(17.0625f, 20.8051f, 17.9245f, 20.1791f, 17.6805f, 19.4261f)
                lineTo(16.9925f, 15.0651f)
                curveTo(16.8825f, 14.7281f, 17.0025f, 14.3591f, 17.2895f, 14.1511f)
                lineTo(20.4135f, 11.0141f)
                curveTo(21.0535f, 10.5491f, 20.7245f, 9.53512f, 19.9325f, 9.53512f)
                lineTo(15.6055f, 8.71812f)
                close()
            }
        }.build()

    val StarOutline: ImageVector
        get() = ImageVector.Builder(
            name = "StarOutline",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).apply {
            path(
                fill = SolidColor(Color.White),
                pathFillType = PathFillType.EvenOdd
            ) {
                moveTo(4.00298f, 10.3318f)
                curveTo(4.01898f, 10.2848f, 4.04298f, 10.2848f, 4.06698f, 10.2848f)
                lineTo(8.47598f, 9.46578f)
                curveTo(9.10198f, 9.43378f, 9.64598f, 9.03378f, 9.86398f, 8.44578f)
                lineTo(11.894f, 4.39978f)
                curveTo(11.943f, 4.27178f, 11.951f, 4.24878f, 12f, 4.24878f)
                curveTo(12.049f, 4.24878f, 12.057f, 4.27178f, 12.065f, 4.29578f)
                lineTo(14.137f, 8.44578f)
                curveTo(14.355f, 9.03378f, 14.899f, 9.43378f, 15.524f, 9.46578f)
                lineTo(19.862f, 10.2848f)
                curveTo(19.957f, 10.2848f, 19.982f, 10.2848f, 19.997f, 10.3318f)
                curveTo(20.012f, 10.3778f, 19.992f, 10.3928f, 19.973f, 10.4068f)
                lineTo(16.793f, 13.5848f)
                curveTo(16.3f, 13.9778f, 16.09f, 14.6228f, 16.259f, 15.2298f)
                lineTo(16.94f, 19.5418f)
                curveTo(16.975f, 19.6798f, 16.982f, 19.7038f, 16.942f, 19.7318f)
                curveTo(16.903f, 19.7618f, 16.883f, 19.7468f, 16.863f, 19.7318f)
                lineTo(12.887f, 17.3898f)
                curveTo(12.352f, 17.0208f, 11.649f, 17.0208f, 11.113f, 17.3898f)
                lineTo(7.19698f, 19.6918f)
                curveTo(7.11698f, 19.7468f, 7.09798f, 19.7618f, 7.05798f, 19.7318f)
                curveTo(7.01898f, 19.7038f, 7.02498f, 19.6798f, 7.03298f, 19.6568f)
                lineTo(7.74098f, 15.2298f)
                curveTo(7.90998f, 14.6228f, 7.69998f, 13.9778f, 7.20698f, 13.5848f)
                lineTo(4.11798f, 10.4838f)
                curveTo(4.00798f, 10.3928f, 3.98798f, 10.3778f, 4.00298f, 10.3318f)
                close()
                moveTo(3.09198f, 11.5788f)
                lineTo(6.17998f, 14.6798f)
                lineTo(6.27098f, 14.7568f)
                curveTo(6.29398f, 14.7738f, 6.30398f, 14.8048f, 6.29498f, 14.8318f)
                lineTo(5.58698f, 19.2588f)
                curveTo(5.40798f, 19.8958f, 5.63498f, 20.5528f, 6.17598f, 20.9458f)
                curveTo(6.45598f, 21.1488f, 6.77598f, 21.2508f, 7.09798f, 21.2508f)
                curveTo(7.40498f, 21.2508f, 7.71298f, 21.1568f, 7.98398f, 20.9698f)
                lineTo(11.899f, 18.6668f)
                lineTo(11.96f, 18.6268f)
                curveTo(11.984f, 18.6088f, 12.016f, 18.6088f, 12.04f, 18.6268f)
                lineTo(16.016f, 20.9698f)
                curveTo(16.57f, 21.3528f, 17.275f, 21.3448f, 17.824f, 20.9458f)
                curveTo(18.365f, 20.5528f, 18.592f, 19.8958f, 18.413f, 19.2588f)
                lineTo(17.732f, 14.9468f)
                lineTo(17.705f, 14.8318f)
                curveTo(17.696f, 14.8048f, 17.706f, 14.7738f, 17.73f, 14.7568f)
                lineTo(20.908f, 11.5788f)
                curveTo(21.428f, 11.1678f, 21.63f, 10.5038f, 21.424f, 9.86778f)
                curveTo(21.218f, 9.23578f, 20.67f, 8.81778f, 20.014f, 8.78678f)
                lineTo(15.676f, 7.96778f)
                lineTo(15.606f, 7.96778f)
                curveTo(15.551f, 7.94878f, 15.541f, 7.92078f, 13.47f, 3.76978f)
                curveTo(13.24f, 3.14678f, 12.67f, 2.74878f, 12f, 2.74878f)
                curveTo(11.33f, 2.74878f, 10.76f, 3.14778f, 10.53f, 3.76978f)
                lineTo(8.50198f, 7.81578f)
                lineTo(8.45898f, 7.92078f)
                curveTo(8.44898f, 7.94878f, 8.42398f, 7.96778f, 8.39498f, 7.96778f)
                lineTo(8.32398f, 8.78678f)
                curveTo(3.32998f, 8.81778f, 2.78198f, 9.23578f, 2.57598f, 9.86778f)
                curveTo(2.36998f, 10.5038f, 2.57198f, 11.1678f, 3.09198f, 11.5788f)
                close()
            }
        }.build()
    val Fire: ImageVector
        get() = ImageVector.Builder(
            name = "Fire",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).apply {
            // Path 1 (Light Blue)
            path(
                fill = SolidColor(Color(0xFF79D0E3)),
                pathFillType = PathFillType.EvenOdd
            ) {
                moveTo(9.34258f, 7.41065f)
                verticalLineTo(5.71445f)
                curveTo(9.34258f, 5.11145f, 8.85538f, 4.62305f, 8.25418f, 4.62305f)
                curveTo(7.65358f, 4.62305f, 7.16638f, 5.11145f, 7.16638f, 5.71445f)
                verticalLineTo(7.42445f)
                curveTo(7.25758f, 7.41545f, 7.34998f, 7.41065f, 7.44298f, 7.41065f)
                horizontalLineTo(9.34258f)
                close()
                moveTo(16.8336f, 7.42085f)
                verticalLineTo(5.71505f)
                curveTo(16.8336f, 5.11205f, 16.3464f, 4.62305f, 15.7452f, 4.62305f)
                curveTo(15.1446f, 4.62305f, 14.6574f, 5.11205f, 14.6574f, 5.71505f)
                verticalLineTo(7.41065f)
                horizontalLineTo(16.6224f)
                curveTo(16.6938f, 7.41065f, 16.7634f, 7.41545f, 16.8336f, 7.42085f)
                close()
            }
            // Path 2 (Blue Body)
            path(
                fill = SolidColor(Color(0xFF409EFC)),
                pathFillType = PathFillType.EvenOdd
            ) {
                moveTo(19.2498f, 9.97018f)
                curveTo(19.2498f, 8.62318f, 18.171f, 7.52098f, 16.8006f, 7.41718f)
                curveTo(16.731f, 7.41178f, 16.6608f, 7.40698f, 16.59f, 7.40698f)
                horizontalLineTo(14.625f)
                horizontalLineTo(9.30964f)
                horizontalLineTo(7.41004f)
                curveTo(7.31704f, 7.40698f, 7.22464f, 7.41178f, 7.13404f, 7.42078f)
                curveTo(5.79484f, 7.55398f, 4.75024f, 8.64418f, 4.75024f, 9.97018f)
                verticalLineTo(12.3204f)
                horizontalLineTo(4.75384f)
                curveTo(4.75384f, 12.3444f, 4.75024f, 12.3678f, 4.75024f, 12.3918f)
                curveTo(4.75024f, 16.2498f, 7.99624f, 19.377f, 12f, 19.377f)
                curveTo(16.0038f, 19.377f, 19.2498f, 16.2498f, 19.2498f, 12.3918f)
                curveTo(19.2498f, 12.3678f, 19.2462f, 12.3444f, 19.2462f, 12.3204f)
                horizontalLineTo(19.2498f)
                verticalLineTo(9.97018f)
                close()
            }
            // Path 3 (White Detail)
            path(
                fill = SolidColor(Color.White),
                pathFillType = PathFillType.EvenOdd
            ) {
                moveTo(13.3315f, 9.76325f)
                lineTo(9.69125f, 13.3368f)
                curveTo(9.57785f, 13.4478f, 9.65825f, 13.6374f, 9.81845f, 13.6374f)
                horizontalLineTo(11.2633f)
                curveTo(11.3899f, 13.6374f, 11.4769f, 13.7628f, 11.4307f, 13.878f)
                lineTo(10.3735f, 16.5636f)
                curveTo(10.3039f, 16.7406f, 10.5301f, 16.8864f, 10.6681f, 16.752f)
                lineTo(14.3089f, 13.1778f)
                curveTo(14.4223f, 13.0668f, 14.3419f, 12.8772f, 14.1817f, 12.8772f)
                horizontalLineTo(12.7363f)
                curveTo(12.6109f, 12.8772f, 12.5233f, 12.7518f, 12.5689f, 12.6366f)
                lineTo(13.6261f, 9.95165f)
                curveTo(13.6771f, 9.82265f, 13.5715f, 9.71045f, 13.4563f, 9.71045f)
                curveTo(13.4137f, 9.71045f, 13.3693f, 9.72665f, 13.3315f, 9.76325f)
                close()
            }
        }.build()

    val InfoOutline: ImageVector
        get() = ImageVector.Builder(
            name = "InfoOutline",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).apply {
            path(
                stroke = SolidColor(Color.White),
                strokeLineWidth = 1.5f
            ) {
                 moveTo(12.375f, 3.25f)
                 curveTo(17.4083f, 3.25f, 21.5f, 7.34284f, 21.5f, 12.375f)
                 curveTo(21.5f, 17.4072f, 17.4083f, 21.5f, 12.375f, 21.5f)
                 curveTo(7.34279f, 21.5f, 3.25f, 17.4072f, 3.25f, 12.375f)
                 curveTo(3.25f, 7.34279f, 7.34279f, 3.25f, 12.375f, 3.25f)
                 close()
                 moveTo(12.375f, 7.86816f)
                 curveTo(12.2372f, 7.86816f, 12.125f, 7.98038f, 12.125f, 8.11816f)
                 curveTo(12.1251f, 8.25588f, 12.2373f, 8.36816f, 12.375f, 8.36816f)
                 curveTo(12.5132f, 8.36816f, 12.6249f, 8.25636f, 12.625f, 8.11816f)
                 curveTo(12.625f, 7.97989f, 12.5133f, 7.86816f, 12.375f, 7.86816f)
                 close()
            }
        }.build()
    val Cellular: ImageVector
        get() = ImageVector.Builder(
            name = "Cellular",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).apply {
            path(fill = SolidColor(Color.Black)) {
                moveTo(5f, 15f)
                curveTo(5f, 14.4477f, 5.44772f, 14f, 6f, 14f)
                curveTo(6.55228f, 14f, 7f, 14.4477f, 7f, 15f)
                verticalLineTo(18f)
                curveTo(7f, 18.5523f, 6.55228f, 19f, 6f, 19f)
                curveTo(5.44772f, 19f, 5f, 18.5523f, 5f, 18f)
                verticalLineTo(15f)
                close()
            }
            path(fill = SolidColor(Color.Black)) {
                moveTo(9f, 12f)
                curveTo(9f, 11.4477f, 9.44772f, 11f, 10f, 11f)
                curveTo(10.5523f, 11f, 11f, 11.4477f, 11f, 12f)
                verticalLineTo(18f)
                curveTo(11f, 18.5523f, 10.5523f, 19f, 10f, 19f)
                curveTo(9.44772f, 19f, 9f, 18.5523f, 9f, 18f)
                verticalLineTo(12f)
                close()
            }
            path(fill = SolidColor(Color.Black)) {
                moveTo(13f, 9f)
                curveTo(13f, 8.44772f, 13.4477f, 8f, 14f, 8f)
                curveTo(14.5523f, 8f, 15f, 8.44772f, 15f, 9f)
                verticalLineTo(18f)
                curveTo(15f, 18.5523f, 14.5523f, 19f, 14f, 19f)
                curveTo(13.4477f, 19f, 13f, 18.5523f, 13f, 18f)
                verticalLineTo(9f)
                close()
            }
            path(fill = SolidColor(Color.Black)) {
                moveTo(17f, 6f)
                curveTo(17f, 5.44772f, 17.4477f, 5f, 18f, 5f)
                curveTo(18.5523f, 5f, 19f, 5.44772f, 19f, 6f)
                verticalLineTo(18f)
                curveTo(19f, 18.5523f, 18.5523f, 19f, 18f, 19f)
                curveTo(17.4477f, 19f, 17f, 18.5523f, 17f, 18f)
                verticalLineTo(6f)
                close()
            }
        }.build()

    val CopyAndPlayRoutine: ImageVector
        get() = ImageVector.Builder(
            name = "CopyAndPlayRoutine",
            defaultWidth = 17.dp,
            defaultHeight = 19.dp,
            viewportWidth = 17f,
            viewportHeight = 19f
        ).apply {
            path(
                fill = SolidColor(Color.Black),
                fillAlpha = 1f,
                stroke = null,
                strokeAlpha = 1f,
                strokeLineWidth = 1.0f,
                strokeLineCap = StrokeCap.Butt,
                strokeLineJoin = StrokeJoin.Miter,
                strokeLineMiter = 1.0f,
                pathFillType = PathFillType.EvenOdd
            ) {
                moveTo(10.8456f, 7.88084f)
                lineTo(15.6194f, 4.76182f)
                curveTo(16.2062f, 4.42324f, 16.2062f, 3.57677f, 15.6194f, 3.23819f)
                lineTo(10.8456f, 0.119166f)
                curveTo(10.2593f, -0.21942f, 9.52588f, 0.203813f, 9.52588f, 0.880985f)
                verticalLineTo(7.11851f)
                curveTo(9.52588f, 7.79619f, 10.2593f, 8.21943f, 10.8456f, 7.88084f)
                close()
            }
            path(fill = SolidColor(Color.Black)) {
                moveTo(11.75f, 16.0117f)
                curveTo(11.75f, 17.3867f, 10.625f, 18.5117f, 9.25f, 18.5117f)
                horizontalLineTo(2.5f)
                curveTo(1.125f, 18.5117f, 0f, 17.3867f, 0f, 16.0117f)
                verticalLineTo(7.26172f)
                curveTo(0f, 5.88672f, 1.125f, 4.76172f, 2.5f, 4.76172f)
                horizontalLineTo(8.52692f)
                verticalLineTo(6.93896f)
                curveTo(8.56048f, 7.89765f, 8.92362f, 8.37796f, 9.29692f, 8.63896f)
                curveTo(9.82426f, 9.00767f, 10.629f, 9.24392f, 11.75f, 8.48897f)
                verticalLineTo(16.0117f)
                close()
            }
            path(fill = SolidColor(Color.Black)) {
                moveTo(13.5969f, 7.2749f)
                verticalLineTo(13.8313f)
                curveTo(13.5969f, 14.3153f, 13.9889f, 14.7063f, 14.4719f, 14.7063f)
                curveTo(14.9549f, 14.7063f, 15.3469f, 14.3153f, 15.3469f, 13.8313f)
                verticalLineTo(6.13037f)
                lineTo(13.5969f, 7.2749f)
                close()
            }
            path(fill = SolidColor(Color.Black)) {
                moveTo(4.49192f, 1.36572f)
                curveTo(4.00792f, 1.36572f, 3.61692f, 1.75772f, 3.61692f, 2.24072f)
                curveTo(3.61692f, 2.72372f, 4.00792f, 3.11572f, 4.49192f, 3.11572f)
                horizontalLineTo(8.52505f)
                verticalLineTo(1.36572f)
                horizontalLineTo(4.49192f)
                close()
            }
        }.build()

    val ReplayWorkout: ImageVector
        get() = ImageVector.Builder(
            name = "ReplayWorkout",
            defaultWidth = 18.dp,
            defaultHeight = 20.dp,
            viewportWidth = 18f,
            viewportHeight = 20f
        ).apply {
            path(fill = SolidColor(Color.Black)) {
                moveTo(6.73972f, 5.71114f)
                curveTo(6.73972f, 5.076f, 7.53942f, 4.73486f, 8.05371f, 5.15057f)
                lineTo(11.4651f, 7.90798f)
                curveTo(11.8311f, 8.20369f, 11.8311f, 8.73341f, 11.4651f, 9.02912f)
                lineTo(8.05371f, 11.7866f)
                curveTo(7.53942f, 12.2023f, 6.73972f, 11.8611f, 6.73972f, 11.226f)
                verticalLineTo(5.71114f)
                close()
                moveTo(3.91801f, 14.958f)
                curveTo(4.13315f, 14.604f, 4.59429f, 14.49f, 4.94829f, 14.7051f)
                curveTo(5.20629f, 14.8611f, 5.47458f, 15.0008f, 5.75058f, 15.1234f)
                curveTo(6.12944f, 15.2914f, 6.30001f, 15.7337f, 6.13201f, 16.1126f)
                curveTo(5.96487f, 16.4914f, 5.52172f, 16.662f, 5.14287f, 16.4948f)
                curveTo(4.80858f, 16.3466f, 4.48372f, 16.1768f, 4.17172f, 15.9883f)
                curveTo(3.81687f, 15.7731f, 3.70372f, 15.312f, 3.91801f, 14.958f)
                close()
                moveTo(1.31915f, 11.6348f)
                curveTo(1.68515f, 11.4403f, 2.13943f, 11.5783f, 2.334f, 11.9443f)
                curveTo(2.47629f, 12.21f, 2.63486f, 12.468f, 2.80972f, 12.714f)
                curveTo(3.04886f, 13.0517f, 2.96915f, 13.5197f, 2.63057f, 13.7597f)
                curveTo(2.29286f, 13.9988f, 1.82486f, 13.9191f, 1.58571f, 13.5814f)
                curveTo(1.374f, 13.2831f, 1.182f, 12.972f, 1.01057f, 12.6497f)
                curveTo(0.815146f, 12.2837f, 0.954003f, 11.8294f, 1.31915f, 11.6348f)
                close()
                moveTo(0.799717f, 7.42455f)
                curveTo(1.21286f, 7.44598f, 1.53086f, 7.79912f, 1.50943f, 8.21312f)
                curveTo(1.50257f, 8.34084f, 1.5f, 8.46684f, 1.5f, 8.61255f)
                curveTo(1.5f, 8.78912f, 1.50686f, 8.96484f, 1.52057f, 9.13969f)
                curveTo(1.55143f, 9.55284f, 1.242f, 9.91284f, 0.829716f, 9.94455f)
                curveTo(0.416573f, 9.97626f, 0.0557156f, 9.66684f, 0.0248585f, 9.25369f)
                curveTo(0.00857276f, 9.04198f, 0f, 8.83027f, 0f, 8.61427f)
                curveTo(0f, 8.44198f, 0.00342961f, 8.28941f, 0.012001f, 8.13427f)
                curveTo(0.0334296f, 7.72112f, 0.385717f, 7.40313f, 0.799717f, 7.42455f)
                close()
                moveTo(1.45629f, 3.78772f)
                curveTo(1.68686f, 3.444f, 2.15315f, 3.35143f, 2.49686f, 3.582f)
                curveTo(2.84143f, 3.81258f, 2.93315f, 4.27886f, 2.70257f, 4.62258f)
                curveTo(2.53457f, 4.87372f, 2.38286f, 5.13514f, 2.24829f, 5.40514f)
                curveTo(2.06314f, 5.77543f, 1.61229f, 5.92543f, 1.242f, 5.74114f)
                curveTo(0.871717f, 5.556f, 0.72086f, 5.10515f, 0.906003f, 4.73486f)
                curveTo(1.06972f, 4.40743f, 1.25314f, 4.092f, 1.45629f, 3.78772f)
                close()
                moveTo(4.93458f, 0.782572f)
                curveTo(5.30915f, 0.605144f, 5.75658f, 0.764571f, 5.93401f, 1.13829f)
                curveTo(6.1123f, 1.512f, 5.95287f, 1.95943f, 5.57915f, 2.13771f)
                curveTo(5.30658f, 2.26714f, 5.04258f, 2.41457f, 4.78801f, 2.57743f)
                curveTo(4.44001f, 2.80114f, 3.97544f, 2.7f, 3.75172f, 2.35114f)
                curveTo(3.52801f, 2.00314f, 3.62915f, 1.53857f, 3.97801f, 1.31486f)
                curveTo(4.28487f, 1.11771f, 4.60458f, 0.940287f, 4.93458f, 0.782572f)
                close()
            }
            path(fill = SolidColor(Color.Black)) {
                moveTo(8.85942f, 0f)
                curveTo(13.6037f, 0f, 17.4497f, 3.84598f, 17.4497f, 8.59027f)
                curveTo(17.4497f, 13.3354f, 14.3429f, 17.1666f, 9.59778f, 17.1675f)
                lineTo(10.1036f, 17.937f)
                curveTo(10.3906f, 18.2354f, 10.3833f, 18.7106f, 10.0849f, 18.9976f)
                curveTo(9.783f, 19.2852f, 9.31071f, 19.2766f, 9.0244f, 18.9789f)
                lineTo(7.27528f, 17.1675f)
                curveTo(7.09173f, 16.9777f, 6.99384f, 16.7271f, 7.00024f, 16.4627f)
                curveTo(7.00665f, 16.1983f, 7.11801f, 15.9528f, 7.30911f, 15.7721f)
                lineTo(9.14466f, 14.0483f)
                curveTo(9.44647f, 13.7649f, 9.92073f, 13.78f, 10.2041f, 14.0818f)
                curveTo(10.4861f, 14.3822f, 10.4732f, 14.8543f, 10.175f, 15.1385f)
                lineTo(9.59778f, 15.7721f)
                curveTo(13.5689f, 15.7721f, 16.05f, 12.5623f, 16.05f, 8.59027f)
                curveTo(16.05f, 4.61912f, 12.8306f, 1.39971f, 8.85942f, 1.39971f)
                curveTo(8.47285f, 1.39971f, 8.15914f, 1.08686f, 8.15914f, 0.700287f)
                curveTo(8.15914f, 0.313715f, 8.47285f, 0f, 8.85942f, 0f)
                close()
            }
        }.build()

    val ListAI: ImageVector
        get() = ImageVector.Builder(
            name = "ListAI",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).apply {
            // Path 1
            path(
                fill = SolidColor(Color.White),
                pathFillType = PathFillType.EvenOdd
            ) {
                moveTo(7.73608f, 15.1797f)
                curveTo(8.42608f, 15.1797f, 8.98608f, 15.7397f, 8.98608f, 16.4297f)
                curveTo(8.98608f, 17.1197f, 8.42608f, 17.6797f, 7.73608f, 17.6797f)
                curveTo(7.04608f, 17.6797f, 6.48608f, 17.1197f, 6.48608f, 16.4297f)
                curveTo(6.48608f, 15.7397f, 7.04608f, 15.1797f, 7.73608f, 15.1797f)
                close()
                moveTo(7.73608f, 6.33691f)
                curveTo(8.42608f, 6.33691f, 8.98608f, 6.89691f, 8.98608f, 7.58691f)
                curveTo(8.98608f, 8.27691f, 8.42608f, 8.83691f, 7.73608f, 8.83691f)
                curveTo(7.04608f, 8.83691f, 6.48608f, 8.27691f, 6.48608f, 7.58691f)
                curveTo(6.48608f, 6.89691f, 7.04608f, 6.33691f, 7.73608f, 6.33691f)
                close()
            }
            // Path 2
            path(
                fill = SolidColor(Color.White),
                pathFillType = PathFillType.NonZero
            ) {
                moveTo(15.7274f, 15.9704f)
                curveTo(15.7971f, 15.8515f, 15.995f, 15.8515f, 16.0647f, 15.9704f)
                lineTo(16.3271f, 16.5426f)
                curveTo(16.5323f, 16.9901f, 16.8953f, 17.3461f, 17.3467f, 17.5426f)
                lineTo(17.7921f, 17.7366f)
                curveTo(17.9307f, 17.7971f, 17.9307f, 17.9661f, 17.7921f, 18.0259f)
                lineTo(17.3467f, 18.2198f)
                curveTo(16.8953f, 18.4163f, 16.5323f, 18.7724f, 16.3271f, 19.2198f)
                lineTo(16.0647f, 19.792f)
                curveTo(15.995f, 19.9109f, 15.7971f, 19.9109f, 15.7274f, 19.792f)
                lineTo(15.465f, 19.2198f)
                curveTo(15.2598f, 18.7724f, 14.8968f, 18.4163f, 14.4454f, 18.2198f)
                lineTo(14f, 18.0259f)
                curveTo(13.8614f, 17.966f, 13.8614f, 17.7971f, 14f, 17.7366f)
                lineTo(14.4454f, 17.5426f)
                curveTo(14.8968f, 17.3461f, 15.2598f, 16.9901f, 15.465f, 16.5426f)
                lineTo(15.7274f, 15.9704f)
                close()
            }
            // Path 3
            path(
                fill = SolidColor(Color.White),
                pathFillType = PathFillType.NonZero
            ) {
                moveTo(19.1593f, 12f)
                curveTo(19.2472f, 11.852f, 19.4935f, 11.852f, 19.5814f, 12f)
                lineTo(19.989f, 12.8893f)
                curveTo(20.1943f, 13.337f, 20.5576f, 13.6933f, 21.0093f, 13.8898f)
                lineTo(21.7407f, 14.2079f)
                curveTo(21.9136f, 14.283f, 21.9136f, 14.495f, 21.7407f, 14.5701f)
                lineTo(21.0092f, 14.8883f)
                curveTo(20.5576f, 15.0847f, 20.1943f, 15.4409f, 19.989f, 15.8885f)
                lineTo(19.5814f, 16.7774f)
                curveTo(19.4935f, 16.9262f, 19.2472f, 16.9262f, 19.1593f, 16.7774f)
                lineTo(18.7517f, 15.8885f)
                curveTo(18.5464f, 15.4409f, 18.1831f, 15.0847f, 17.7315f, 14.8883f)
                lineTo(17f, 14.5701f)
                curveTo(16.8271f, 14.495f, 16.8271f, 14.283f, 17f, 14.2079f)
                lineTo(17.7314f, 13.8898f)
                curveTo(18.1831f, 13.6933f, 18.5464f, 13.337f, 18.7517f, 12.8893f)
                lineTo(19.1593f, 12f)
                close()
            }
            // Path 4
            path(
                stroke = SolidColor(Color.White),
                strokeLineWidth = 1.0f,
                strokeLineCap = StrokeCap.Round
            ) {
                moveTo(13.3904f, 19.792f)
                horizontalLineTo(6.5004f)
                curveTo(5.1194f, 19.792f, 4.0004f, 18.673f, 4.0004f, 17.292f)
                verticalLineTo(15.563f)
                curveTo(4.0004f, 14.183f, 5.1194f, 13.063f, 6.5004f, 13.063f)
                lineTo(13.3904f, 13.092f)
                moveTo(6.5f, 10.9492f)
                curveTo(5.119f, 10.9492f, 4f, 9.83022f, 4f, 8.44922f)
                verticalLineTo(6.72021f)
                curveTo(4f, 5.34021f, 5.119f, 4.22021f, 6.5f, 4.22021f)
                horizontalLineTo(17.5f)
                curveTo(18.881f, 4.22021f, 20f, 5.34021f, 20f, 6.72021f)
                verticalLineTo(8.44922f)
                curveTo(20f, 9.83022f, 18.881f, 10.9492f, 17.5f, 10.9492f)
                horizontalLineTo(6.5f)
                close()
                moveTo(15.7274f, 15.9704f)
                curveTo(15.7971f, 15.8515f, 15.995f, 15.8515f, 16.0647f, 15.9704f)
                lineTo(16.3271f, 16.5426f)
                curveTo(16.5323f, 16.9901f, 16.8953f, 17.3461f, 17.3467f, 17.5426f)
                lineTo(17.7921f, 17.7366f)
                curveTo(17.9307f, 17.7971f, 17.9307f, 17.9661f, 17.7921f, 18.0259f)
                lineTo(17.3467f, 18.2198f)
                curveTo(16.8953f, 18.4163f, 16.5323f, 18.7724f, 16.3271f, 19.2198f)
                lineTo(16.0647f, 19.792f)
                curveTo(15.995f, 19.9109f, 15.7971f, 19.9109f, 15.7274f, 19.792f)
                lineTo(15.465f, 19.2198f)
                curveTo(15.2598f, 18.7724f, 14.8968f, 18.4163f, 14.4454f, 18.2198f)
                lineTo(14f, 18.0259f)
                curveTo(13.8614f, 17.966f, 13.8614f, 17.7971f, 14f, 17.7366f)
                lineTo(14.4454f, 17.5426f)
                curveTo(14.8968f, 17.3461f, 15.2598f, 16.9901f, 15.465f, 16.5426f)
                lineTo(15.7274f, 15.9704f)
                close()
                moveTo(19.1593f, 12f)
                curveTo(19.2472f, 11.852f, 19.4935f, 11.852f, 19.5814f, 12f)
                lineTo(19.989f, 12.8893f)
                curveTo(20.1943f, 13.337f, 20.5576f, 13.6933f, 21.0093f, 13.8898f)
                lineTo(21.7407f, 14.2079f)
                curveTo(21.9136f, 14.283f, 21.9136f, 14.495f, 21.7407f, 14.5701f)
                lineTo(21.0092f, 14.8883f)
                curveTo(20.5576f, 15.0847f, 20.1943f, 15.4409f, 19.989f, 15.8885f)
                lineTo(19.5814f, 16.7774f)
                curveTo(19.4935f, 16.9262f, 19.2472f, 16.9262f, 19.1593f, 16.7774f)
                lineTo(18.7517f, 15.8885f)
                curveTo(18.5464f, 15.4409f, 18.1831f, 15.0847f, 17.7315f, 14.8883f)
                lineTo(17f, 14.5701f)
                curveTo(16.8271f, 14.495f, 16.8271f, 14.283f, 17f, 14.2079f)
                lineTo(17.7314f, 13.8898f)
                curveTo(18.1831f, 13.6933f, 18.5464f, 13.337f, 18.7517f, 12.8893f)
                lineTo(19.1593f, 12f)
                close()
            }
        }.build()

    val ShowRoutineList: ImageVector
        get() = ImageVector.Builder(
            name = "ShowRoutineList",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).apply {
            // Path 1
            path(
                fill = SolidColor(Color.White),
                pathFillType = PathFillType.EvenOdd
            ) {
                moveTo(7.73608f, 15.1797f)
                curveTo(8.42608f, 15.1797f, 8.98608f, 15.7397f, 8.98608f, 16.4297f)
                curveTo(8.98608f, 17.1197f, 8.42608f, 17.6797f, 7.73608f, 17.6797f)
                curveTo(7.04608f, 17.6797f, 6.48608f, 17.1197f, 6.48608f, 16.4297f)
                curveTo(6.48608f, 15.7397f, 7.04608f, 15.1797f, 7.73608f, 15.1797f)
                close()
                moveTo(7.73608f, 6.33691f)
                curveTo(8.42608f, 6.33691f, 8.98608f, 6.89691f, 8.98608f, 7.58691f)
                curveTo(8.98608f, 8.27691f, 8.42608f, 8.83691f, 7.73608f, 8.83691f)
                curveTo(7.04608f, 8.83691f, 6.48608f, 8.27691f, 6.48608f, 7.58691f)
                curveTo(6.48608f, 6.89691f, 7.04608f, 6.33691f, 7.73608f, 6.33691f)
                close()
            }
            // Path 2
            path(
                stroke = SolidColor(Color.White),
                strokeLineWidth = 1.0f,
                pathFillType = PathFillType.EvenOdd
            ) {
                 moveTo(6.5f, 10.9492f)
                 curveTo(5.119f, 10.9492f, 4f, 9.83022f, 4f, 8.44922f)
                 verticalLineTo(6.72021f)
                 curveTo(4f, 5.34021f, 5.119f, 4.22021f, 6.5f, 4.22021f)
                 horizontalLineTo(17.5f)
                 curveTo(18.881f, 4.22021f, 20f, 5.34021f, 20f, 6.72021f)
                 verticalLineTo(8.44922f)
                 curveTo(20f, 9.83022f, 18.881f, 10.9492f, 17.5f, 10.9492f)
                 horizontalLineTo(6.5f)
                 close()
            }
            // Path 3
            path(
                stroke = SolidColor(Color.White),
                strokeLineWidth = 1.0f,
                strokeLineCap = StrokeCap.Round
            ) {
                moveTo(13.3904f, 19.792f)
                horizontalLineTo(6.50037f)
                curveTo(5.11937f, 19.792f, 4.00037f, 18.673f, 4.00037f, 17.292f)
                verticalLineTo(15.563f)
                curveTo(4.00037f, 14.183f, 5.11937f, 13.063f, 6.50037f, 13.063f)
                horizontalLineTo(10.9288f)
            }
            // Path 4
            path(
                fill = SolidColor(Color.White),
                pathFillType = PathFillType.EvenOdd
            ) {
                moveTo(14.1511f, 15.0065f)
                curveTo(14.354f, 14.9968f, 14.558f, 15.0248f, 14.727f, 15.1249f)
                lineTo(20.1534f, 18.7226f)
                curveTo(20.3418f, 18.8244f, 20.4699f, 19.0111f, 20.4952f, 19.2226f)
                curveTo(20.5388f, 19.5827f, 20.2815f, 19.9109f, 19.922f, 19.954f)
                curveTo(19.7762f, 19.9723f, 19.6325f, 19.9416f, 19.5065f, 19.8652f)
                lineTo(16.2473f, 17.7136f)
                lineTo(15.7232f, 18.5838f)
                lineTo(17.1601f, 18.9686f)
                curveTo(17.4012f, 19.0482f, 17.5766f, 19.2624f, 17.6073f, 19.5143f)
                curveTo(17.6504f, 19.8738f, 17.3926f, 20.2016f, 17.0331f, 20.2451f)
                curveTo(16.933f, 20.2575f, 16.8329f, 20.2462f, 16.7322f, 20.2112f)
                lineTo(14.6436f, 19.6375f)
                curveTo(14.3707f, 19.5568f, 14.1667f, 19.3625f, 14.0839f, 19.1037f)
                curveTo(14.001f, 18.8453f, 14.0526f, 18.5676f, 14.2292f, 18.3395f)
                lineTo(15.1322f, 16.958f)
                lineTo(14.3691f, 16.4413f)
                lineTo(13.1141f, 17.3035f)
                curveTo(12.8643f, 17.48f, 12.7126f, 17.5844f, 12.4752f, 17.6172f)
                lineTo(12.4607f, 17.6188f)
                curveTo(12.2277f, 17.6468f, 11.9871f, 17.5795f, 11.7982f, 17.4315f)
                lineTo(10.8316f, 16.6205f)
                curveTo(10.6497f, 16.4574f, 10.5421f, 16.3304f, 10.5055f, 16.0404f)
                curveTo(10.4592f, 15.6744f, 10.7111f, 15.4274f, 11.0227f, 15.429f)
                curveTo(11.3714f, 15.4311f, 11.3709f, 15.4839f, 11.5937f, 15.5883f)
                lineTo(12.3122f, 16.2061f)
                lineTo(13.4811f, 15.3547f)
                curveTo(13.7583f, 15.1448f, 13.9552f, 15.0162f, 14.1511f, 15.0065f)
                close()
                moveTo(16.2912f, 13.6823f)
                curveTo(16.5086f, 13.5477f, 16.7896f, 13.545f, 16.9871f, 13.6753f)
                curveTo(17.1873f, 13.8066f, 17.2906f, 14.0477f, 17.2578f, 14.3033f)
                lineTo(16.9709f, 15.6612f)
                lineTo(14.9404f, 14.4255f)
                lineTo(16.2912f, 13.6823f)
                close()
                moveTo(13.0726f, 12.5f)
                curveTo(13.6678f, 12.5f, 14.1522f, 12.9849f, 14.1522f, 13.5807f)
                curveTo(14.1522f, 14.1748f, 13.6678f, 14.6586f, 13.0726f, 14.6586f)
                curveTo(12.4779f, 14.6586f, 11.993f, 14.1748f, 11.993f, 13.5807f)
                curveTo(11.993f, 12.9849f, 12.4779f, 12.5f, 13.0726f, 12.5f)
                close()
            }
        }.build()

    // ============================================================
    // CATEGORY ICONS — mapped via CategorySeeder iconKey values
    // ============================================================

    /** ic_food — burger / sandwich */
    val Food: ImageVector
        get() = ImageVector.Builder(
            name = "Food",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).apply {
            path(
                stroke = SolidColor(Color.White),
                strokeLineWidth = 1.5f,
                strokeLineCap = StrokeCap.Round,
                strokeLineJoin = StrokeJoin.Round
            ) {
                moveTo(20.25f, 13.4997f)
                horizontalLineTo(17.714f)
                moveTo(13.4156f, 13.4997f)
                horizontalLineTo(4.24963f)
                moveTo(20.25f, 16.156f)
                curveTo(20.25f, 17.69f, 19.246f, 19.511f, 12.251f, 19.511f)
                curveTo(5.25403f, 19.511f, 4.25003f, 17.69f, 4.25003f, 16.156f)
                horizontalLineTo(20.25f)
                moveTo(12.2505f, 4.24969f)
                curveTo(17.1845f, 4.24969f, 20.2505f, 7.09169f, 20.2505f, 10.8447f)
                horizontalLineTo(4.25053f)
                curveTo(4.25053f, 7.09169f, 7.31553f, 4.24969f, 12.2505f, 4.24969f)
                close()
            }
            path(
                stroke = SolidColor(Color.White),
                strokeLineWidth = 1.5f,
                strokeLineCap = StrokeCap.Round,
                strokeLineJoin = StrokeJoin.Round
            ) {
                moveTo(11.0591f, 10.8444f)
                lineTo(16.4081f, 15.1164f)
                lineTo(19.0131f, 10.8444f)
            }
        }.build()

    /** ic_shopping — shopping cart */
    val Shopping: ImageVector
        get() = ImageVector.Builder(
            name = "Shopping",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).apply {
            path(
                fill = SolidColor(Color.White),
                pathFillType = PathFillType.EvenOdd
            ) {
                moveTo(17.5066f, 17.1421f)
                curveTo(18.4036f, 17.1421f, 19.1326f, 17.8701f, 19.1326f, 18.7661f)
                curveTo(19.1326f, 19.6621f, 18.4036f, 20.3921f, 17.5066f, 20.3921f)
                curveTo(16.6116f, 20.3921f, 15.8826f, 19.6621f, 15.8826f, 18.7661f)
                curveTo(15.8826f, 17.8701f, 16.6116f, 17.1421f, 17.5066f, 17.1421f)
                close()
                moveTo(10.2683f, 17.1421f)
                curveTo(11.1643f, 17.1421f, 11.8933f, 17.8701f, 11.8933f, 18.7661f)
                curveTo(11.8933f, 19.6621f, 11.1643f, 20.3921f, 10.2683f, 20.3921f)
                curveTo(9.3723f, 20.3921f, 8.6443f, 19.6621f, 8.6443f, 18.7661f)
                curveTo(8.6443f, 17.8701f, 9.3723f, 17.1421f, 10.2683f, 17.1421f)
                close()
                moveTo(5.1664f, 3.60791f)
                curveTo(5.9454f, 3.60791f, 6.6374f, 4.12991f, 6.8504f, 4.87891f)
                lineTo(7.5044f, 7.18391f)
                horizontalLineTo(19.8594f)
                curveTo(20.8524f, 7.18391f, 21.5714f, 8.13091f, 21.3044f, 9.08691f)
                lineTo(20.1054f, 13.3859f)
                curveTo(19.8034f, 14.4669f, 18.8194f, 15.2139f, 17.6974f, 15.2139f)
                horizontalLineTo(10.0954f)
                curveTo(8.9774f, 15.2139f, 7.9944f, 14.4719f, 7.6904f, 13.3959f)
                lineTo(5.9314f, 7.18391f)
                horizontalLineTo(5.9464f)
                lineTo(5.4084f, 5.28991f)
                curveTo(5.3764f, 5.18191f, 5.2784f, 5.10791f, 5.1664f, 5.10791f)
                horizontalLineTo(3.3894f)
                curveTo(2.9754f, 5.10791f, 2.6394f, 4.77191f, 2.6394f, 4.35791f)
                curveTo(2.6394f, 3.94391f, 2.9754f, 3.60791f, 3.3894f, 3.60791f)
                horizontalLineTo(5.1664f)
                close()
            }
        }.build()

    /** ic_car — automobile */
    val Car: ImageVector
        get() = ImageVector.Builder(
            name = "Car",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).apply {
            path(
                fill = SolidColor(Color.White)
            ) {
                moveTo(5.74249f, 9.28949f)
                curveTo(5.72649f, 9.28949f, 5.71149f, 9.29249f, 5.69549f, 9.29249f)
                lineTo(6.65749f, 6.84749f)
                curveTo(6.92249f, 6.17349f, 7.56149f, 5.73749f, 8.28649f, 5.73749f)
                horizontalLineTo(15.7135f)
                curveTo(16.4375f, 5.73749f, 17.0765f, 6.17349f, 17.3425f, 6.84749f)
                lineTo(18.3035f, 9.29249f)
                curveTo(18.2875f, 9.29249f, 18.2725f, 9.28949f, 18.2575f, 9.28949f)
                horizontalLineTo(5.74249f)
                close()
                moveTo(17.5975f, 12.5475f)
                horizontalLineTo(20.7575f)
                verticalLineTo(11.7895f)
                curveTo(20.7575f, 11.3135f, 20.6195f, 10.8695f, 20.3865f, 10.4895f)
                lineTo(18.7375f, 6.29849f)
                curveTo(18.2455f, 5.04749f, 17.0575f, 4.23749f, 15.7135f, 4.23749f)
                horizontalLineTo(8.28649f)
                curveTo(6.94049f, 4.23749f, 5.75449f, 5.04749f, 5.26249f, 6.29849f)
                lineTo(3.61349f, 10.4885f)
                curveTo(3.38049f, 10.8695f, 3.24249f, 11.3135f, 3.24249f, 11.7895f)
                verticalLineTo(12.5475f)
                horizontalLineTo(6.40149f)
                curveTo(6.81549f, 12.5475f, 7.15149f, 12.8835f, 7.15149f, 13.2975f)
                curveTo(7.15149f, 13.7115f, 6.81549f, 14.0475f, 6.40149f, 14.0475f)
                horizontalLineTo(3.24249f)
                verticalLineTo(14.8045f)
                curveTo(3.24249f, 15.5215f, 3.55049f, 16.1695f, 4.03849f, 16.6265f)
                verticalLineTo(18.5125f)
                curveTo(4.03849f, 19.2025f, 4.59749f, 19.7625f, 5.28849f, 19.7625f)
                horizontalLineTo(5.78849f)
                curveTo(6.47849f, 19.7625f, 7.03849f, 19.2025f, 7.03849f, 18.5125f)
                verticalLineTo(17.3045f)
                horizontalLineTo(16.9615f)
                verticalLineTo(18.5125f)
                curveTo(16.9615f, 19.2025f, 17.5205f, 19.7625f, 18.2115f, 19.7625f)
                horizontalLineTo(18.7115f)
                curveTo(19.4015f, 19.7625f, 19.9615f, 19.2025f, 19.9615f, 18.5125f)
                verticalLineTo(16.6265f)
                curveTo(20.4485f, 16.1695f, 20.7575f, 15.5215f, 20.7575f, 14.8045f)
                verticalLineTo(14.0475f)
                horizontalLineTo(17.5975f)
                curveTo(17.1825f, 14.0475f, 16.8475f, 13.7115f, 16.8475f, 13.2975f)
                curveTo(16.8475f, 12.8835f, 17.1825f, 12.5475f, 17.5975f, 12.5475f)
                close()
            }
        }.build()

    /** ic_home — house */
    val Home: ImageVector
        get() = ImageVector.Builder(
            name = "Home",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).apply {
            path(
                fill = SolidColor(Color.White),
                pathFillType = PathFillType.EvenOdd
            ) {
                moveTo(18.7204f, 8.46198f)
                lineTo(13.6396f, 3.70139f)
                curveTo(12.6534f, 2.77675f, 11.1544f, 2.7647f, 10.1543f, 3.67487f)
                lineTo(4.89313f, 8.45957f)
                curveTo(4.32562f, 8.97553f, 4f, 9.71934f, 4f, 10.5017f)
                verticalLineTo(17.2791f)
                curveTo(4f, 18.7824f, 5.17573f, 20f, 6.62475f, 20f)
                horizontalLineTo(8.25751f)
                curveTo(8.80874f, 20f, 9.25647f, 19.5359f, 9.25647f, 18.9645f)
                verticalLineTo(13.3335f)
                curveTo(9.25647f, 12.8513f, 9.60535f, 12.3691f, 10.1868f, 12.3691f)
                horizontalLineTo(13.3977f)
                curveTo(13.9117f, 12.3691f, 14.328f, 12.8018f, 14.328f, 13.3347f)
                verticalLineTo(18.9645f)
                curveTo(14.328f, 19.5359f, 14.7758f, 20f, 15.327f, 20f)
                horizontalLineTo(16.9598f)
                curveTo(18.4088f, 20f, 19.5833f, 18.7824f, 19.5833f, 17.2791f)
                verticalLineTo(10.4776f)
                curveTo(19.5833f, 9.70969f, 19.2705f, 8.97674f, 18.7204f, 8.46198f)
                close()
            }
        }.build()

    /** ic_game — game controller */
    val Game: ImageVector
        get() = ImageVector.Builder(
            name = "Game",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).apply {
            path(
                fill = SolidColor(Color.White)
            ) {
                moveTo(16.8059f, 13.18f)
                curveTo(16.1719f, 13.18f, 15.6599f, 12.664f, 15.6599f, 12.028f)
                curveTo(15.6599f, 11.391f, 16.1719f, 10.875f, 16.8059f, 10.875f)
                curveTo(17.4389f, 10.875f, 17.9519f, 11.391f, 17.9519f, 12.028f)
                curveTo(17.9519f, 12.664f, 17.4389f, 13.18f, 16.8059f, 13.18f)
                close()
                moveTo(14.2709f, 10.431f)
                curveTo(13.6389f, 10.431f, 13.1249f, 9.915f, 13.1249f, 9.278f)
                curveTo(13.1249f, 8.642f, 13.6389f, 8.126f, 14.2709f, 8.126f)
                curveTo(14.9049f, 8.126f, 15.4169f, 8.642f, 15.4169f, 9.278f)
                curveTo(15.4169f, 9.915f, 14.9049f, 10.431f, 14.2709f, 10.431f)
                close()
                moveTo(10.6009f, 11.146f)
                horizontalLineTo(9.3509f)
                verticalLineTo(12.396f)
                curveTo(9.3509f, 12.81f, 9.0149f, 13.146f, 8.6009f, 13.146f)
                curveTo(8.1859f, 13.146f, 7.8509f, 12.81f, 7.8509f, 12.396f)
                verticalLineTo(11.146f)
                horizontalLineTo(6.6009f)
                curveTo(6.1859f, 11.146f, 5.8509f, 10.81f, 5.8509f, 10.396f)
                curveTo(5.8509f, 9.982f, 6.1859f, 9.646f, 6.6009f, 9.646f)
                horizontalLineTo(7.8509f)
                verticalLineTo(8.396f)
                curveTo(7.8509f, 7.982f, 8.1859f, 7.646f, 8.6009f, 7.646f)
                curveTo(9.0149f, 7.646f, 9.3509f, 7.982f, 9.3509f, 8.396f)
                verticalLineTo(9.646f)
                horizontalLineTo(10.6009f)
                curveTo(11.0149f, 9.646f, 11.3509f, 9.982f, 11.3509f, 10.396f)
                curveTo(11.3509f, 10.81f, 11.0149f, 11.146f, 10.6009f, 11.146f)
                close()
                moveTo(20.6729f, 11.208f)
                curveTo(20.6729f, 7.785f, 17.8889f, 5f, 14.4659f, 5f)
                horizontalLineTo(9.53391f)
                curveTo(6.1109f, 5f, 3.3269f, 7.785f, 3.3269f, 11.208f)
                verticalLineTo(16.664f)
                curveTo(3.3269f, 18.107f, 4.5439f, 19f, 5.6719f, 19f)
                curveTo(6.2559f, 19f, 6.8189f, 18.774f, 7.2549f, 18.364f)
                lineTo(9.9839f, 15.802f)
                horizontalLineTo(14.0149f)
                lineTo(16.7439f, 18.364f)
                curveTo(17.1869f, 18.781f, 17.7349f, 19f, 18.3269f, 19f)
                curveTo(19.4559f, 19f, 20.6729f, 18.107f, 20.6729f, 16.664f)
                verticalLineTo(11.208f)
                close()
            }
        }.build()

    /** ic_laptop — laptop computer */
    val Laptop: ImageVector
        get() = ImageVector.Builder(
            name = "Laptop",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).apply {
            path(
                stroke = SolidColor(Color.White),
                strokeLineWidth = 1.75f,
                strokeLineCap = StrokeCap.Round,
                strokeLineJoin = StrokeJoin.Round
            ) {
                moveTo(21f, 19.1503f)
                horizontalLineTo(3f)
            }
            path(
                fill = SolidColor(Color.White),
                pathFillType = PathFillType.EvenOdd
            ) {
                moveTo(4.49996f, 3.60001f)
                horizontalLineTo(19.5f)
                curveTo(20.6048f, 3.60001f, 21.4997f, 4.49572f, 21.4997f, 5.59972f)
                verticalLineTo(14.7f)
                curveTo(21.4997f, 15.8049f, 20.6048f, 16.6997f, 19.5f, 16.6997f)
                horizontalLineTo(4.49996f)
                curveTo(3.3951f, 16.6997f, 2.50024f, 15.8049f, 2.50024f, 14.7f)
                verticalLineTo(5.59972f)
                curveTo(2.50024f, 4.49572f, 3.3951f, 3.60001f, 4.49996f, 3.60001f)
                close()
            }
        }.build()

    /** ic_heart — health / heart */
    val Heart: ImageVector
        get() = ImageVector.Builder(
            name = "Heart",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).apply {
            path(
                fill = SolidColor(Color.White),
                pathFillType = PathFillType.EvenOdd
            ) {
                moveTo(10.72f, 18.38f)
                curveTo(11.44f, 19f, 12.49f, 19f, 13.21f, 18.38f)
                curveTo(14.91f, 16.9f, 17.87f, 14.24f, 18.92f, 12.75f)
                curveTo(19.01f, 12.62f, 19.28f, 12.2f, 19.28f, 12.2f)
                curveTo(19.69f, 11.48f, 19.93f, 10.64f, 19.93f, 9.73f)
                curveTo(19.93f, 7.11f, 17.97f, 5f, 15.55f, 5f)
                curveTo(14.07f, 5f, 12.76f, 5.78999f, 11.97f, 7.00999f)
                curveTo(11.17f, 5.78999f, 9.86f, 5f, 8.38f, 5f)
                curveTo(5.96f, 5f, 4f, 7.11f, 4f, 9.73f)
                curveTo(4f, 10.64f, 4.24f, 11.48f, 4.65f, 12.2f)
                curveTo(4.65f, 12.2f, 4.92f, 12.62f, 5.02f, 12.75f)
                curveTo(6.06f, 14.24f, 9.02f, 16.9f, 10.72f, 18.38f)
                close()
            }
        }.build()

    /** ic_sim — SIM card / recharges */
    val Sim: ImageVector
        get() = ImageVector.Builder(
            name = "Sim",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).apply {
            path(
                fill = SolidColor(Color.White),
                pathFillType = PathFillType.EvenOdd
            ) {
                moveTo(4.85002f, 1.90002f)
                horizontalLineTo(15.45f)
                curveTo(15.8f, 1.90002f, 16.2266f, 2.05312f, 16.5287f, 2.29242f)
                lineTo(16.65f, 2.40002f)
                lineTo(20.25f, 6.20002f)
                curveTo(20.5125f, 6.46252f, 20.6984f, 6.87812f, 20.8078f, 7.31292f)
                lineTo(20.85f, 7.50002f)
                verticalLineTo(20.4f)
                curveTo(20.85f, 21.2526f, 20.132f, 22.0155f, 19.2912f, 22.0935f)
                lineTo(19.15f, 22.1f)
                horizontalLineTo(4.85002f)
                curveTo(3.99742f, 22.1f, 3.23452f, 21.382f, 3.15652f, 20.5412f)
                lineTo(3.15002f, 20.4f)
                verticalLineTo(3.60002f)
                curveTo(3.15002f, 2.74742f, 3.86802f, 1.98452f, 4.70882f, 1.90652f)
                lineTo(4.85002f, 1.90002f)
                close()
                moveTo(16.15f, 8.70002f)
                horizontalLineTo(7.85002f)
                curveTo(6.95002f, 8.70002f, 6.15002f, 9.40002f, 6.15002f, 10.4f)
                verticalLineTo(18.1f)
                curveTo(6.15002f, 19f, 6.85002f, 19.8f, 7.85002f, 19.8f)
                horizontalLineTo(16.15f)
                curveTo(17.05f, 19.8f, 17.85f, 19.1f, 17.85f, 18.1f)
                verticalLineTo(10.4f)
                curveTo(17.85f, 9.50002f, 17.15f, 8.70002f, 16.15f, 8.70002f)
                close()
                moveTo(9.35002f, 10.3f)
                verticalLineTo(13.1f)
                horizontalLineTo(11.05f)
                verticalLineTo(10.3f)
                horizontalLineTo(12.75f)
                verticalLineTo(13.1f)
                horizontalLineTo(14.45f)
                verticalLineTo(10.3f)
                horizontalLineTo(16.05f)
                lineTo(16.15f, 18.1f)
                curveTo(16.15f, 18.2f, 16.05f, 18.2f, 16.05f, 18.2f)
                horizontalLineTo(14.45f)
                verticalLineTo(15.4f)
                horizontalLineTo(12.75f)
                verticalLineTo(18.2f)
                horizontalLineTo(11.05f)
                verticalLineTo(15.4f)
                horizontalLineTo(9.35002f)
                verticalLineTo(18.2f)
                horizontalLineTo(7.75002f)
                curveTo(7.65002f, 18.2f, 7.65002f, 18.1f, 7.65002f, 18.1f)
                verticalLineTo(10.4f)
                curveTo(7.65002f, 10.3f, 7.75002f, 10.3f, 7.75002f, 10.3f)
                horizontalLineTo(9.35002f)
                close()
            }
        }.build()

    /** ic_work — briefcase / office building */
    val Work: ImageVector
        get() = ImageVector.Builder(
            name = "Work",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).apply {
            path(
                fill = SolidColor(Color.White),
                pathFillType = PathFillType.EvenOdd
            ) {
                moveTo(11.771f, 11.646f)
                horizontalLineTo(11.445f)
                curveTo(11.031f, 11.646f, 10.695f, 11.31f, 10.695f, 10.896f)
                curveTo(10.695f, 10.481f, 11.031f, 10.146f, 11.445f, 10.146f)
                horizontalLineTo(11.771f)
                curveTo(12.185f, 10.146f, 12.521f, 10.481f, 12.521f, 10.896f)
                curveTo(12.521f, 11.31f, 12.185f, 11.646f, 11.771f, 11.646f)
                close()
                moveTo(11.445f, 6.896f)
                horizontalLineTo(11.771f)
                curveTo(12.185f, 6.896f, 12.521f, 7.231f, 12.521f, 7.646f)
                curveTo(12.521f, 8.06f, 12.185f, 8.396f, 11.771f, 8.396f)
                horizontalLineTo(11.445f)
                curveTo(11.031f, 8.396f, 10.695f, 8.06f, 10.695f, 7.646f)
                curveTo(10.695f, 7.231f, 11.031f, 6.896f, 11.445f, 6.896f)
                close()
                moveTo(8.271f, 8.396f)
                horizontalLineTo(7.945f)
                curveTo(7.531f, 8.396f, 7.195f, 8.06f, 7.195f, 7.646f)
                curveTo(7.195f, 7.231f, 7.531f, 6.896f, 7.945f, 6.896f)
                horizontalLineTo(8.271f)
                curveTo(8.685f, 6.896f, 9.021f, 7.231f, 9.021f, 7.646f)
                curveTo(9.021f, 8.06f, 8.685f, 8.396f, 8.271f, 8.396f)
                close()
                moveTo(8.271f, 11.646f)
                horizontalLineTo(7.945f)
                curveTo(7.531f, 11.646f, 7.195f, 11.31f, 7.195f, 10.896f)
                curveTo(7.195f, 10.481f, 7.531f, 10.146f, 7.945f, 10.146f)
                horizontalLineTo(8.271f)
                curveTo(8.685f, 10.146f, 9.021f, 10.481f, 9.021f, 10.896f)
                curveTo(9.021f, 11.31f, 8.685f, 11.646f, 8.271f, 11.646f)
                close()
                moveTo(12.582f, 20f)
                horizontalLineTo(17.082f)
                curveTo(18.486f, 20f, 19.625f, 18.915f, 19.625f, 17.575f)
                verticalLineTo(11.927f)
                curveTo(19.625f, 10.587f, 18.486f, 9.5f, 17.082f, 9.5f)
                horizontalLineTo(15.125f)
                verticalLineTo(6.427f)
                curveTo(15.125f, 5.087f, 13.986f, 4f, 12.582f, 4f)
                horizontalLineTo(6.917f)
                curveTo(5.512f, 4f, 4.375f, 5.087f, 4.375f, 6.427f)
                verticalLineTo(17.575f)
                curveTo(4.375f, 18.915f, 5.512f, 20f, 6.917f, 20f)
                horizontalLineTo(8.533f)
                verticalLineTo(15.567f)
                curveTo(8.533f, 15.015f, 8.98f, 14.567f, 9.533f, 14.567f)
                horizontalLineTo(9.625f)
                horizontalLineTo(11.283f)
                curveTo(11.834f, 14.567f, 12.283f, 15.015f, 12.283f, 15.567f)
                verticalLineTo(20f)
                horizontalLineTo(12.582f)
                close()
            }
        }.build()

    /** ic_box — package / misc category */
    val Box: ImageVector
        get() = ImageVector.Builder(
            name = "Box",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).apply {
            path(
                fill = SolidColor(Color.White),
                pathFillType = PathFillType.EvenOdd
            ) {
                moveTo(11.6441f, 3.26102f)
                lineTo(4.56953f, 7.14782f)
                curveTo(4.34033f, 7.27382f, 4.19813f, 7.51442f, 4.19873f, 7.77602f)
                lineTo(4.21013f, 16.0674f)
                curveTo(4.21073f, 16.3236f, 4.34813f, 16.5606f, 4.57073f, 16.6878f)
                lineTo(11.6567f, 20.733f)
                curveTo(11.8769f, 20.859f, 12.1475f, 20.8584f, 12.3677f, 20.7324f)
                lineTo(19.4423f, 16.6668f)
                curveTo(19.6643f, 16.539f, 19.8011f, 16.302f, 19.8011f, 16.0452f)
                lineTo(19.7897f, 7.75442f)
                curveTo(19.7891f, 7.49282f, 19.6463f, 7.25222f, 19.4171f, 7.12742f)
                lineTo(12.3317f, 3.26042f)
                curveTo(12.1169f, 3.14342f, 11.8583f, 3.14402f, 11.6441f, 3.26102f)
                close()
            }
            path(
                fill = SolidColor(Color.White),
                pathFillType = PathFillType.EvenOdd
            ) {
                moveTo(4.3055f, 7.39795f)
                curveTo(4.2407f, 7.50715f, 4.2041f, 7.63315f, 4.2041f, 7.76515f)
                verticalLineTo(16.0565f)
                curveTo(4.2041f, 16.3127f, 4.3415f, 16.5497f, 4.5641f, 16.6769f)
                lineTo(11.6441f, 20.7324f)
                curveTo(11.7545f, 20.7959f, 11.8775f, 20.8272f, 11.9999f, 20.8272f)
                verticalLineTo(11.7329f)
                lineTo(4.3055f, 7.39795f)
                close()
            }
            path(
                fill = SolidColor(Color.White),
                pathFillType = PathFillType.EvenOdd
            ) {
                moveTo(12f, 11.7335f)
                verticalLineTo(20.8272f)
                curveTo(12.123f, 20.8272f, 12.2454f, 20.7954f, 12.3552f, 20.7324f)
                lineTo(19.4352f, 16.6769f)
                curveTo(19.6578f, 16.5497f, 19.7952f, 16.3127f, 19.7952f, 16.0565f)
                verticalLineTo(7.76515f)
                curveTo(19.7952f, 7.63315f, 19.7586f, 7.50715f, 19.6938f, 7.39795f)
                lineTo(12f, 11.7335f)
                close()
            }
        }.build()

    // ============================================================
    // NAVIGATION / MENU ICONS
    // ============================================================

    /** Samsung account / user profile circle icon */
    val SamsungAccount2: ImageVector
        get() = ImageVector.Builder(
            name = "SamsungAccount2",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).apply {
            path(
                fill = SolidColor(Color.White),
                pathFillType = PathFillType.EvenOdd
            ) {
                moveTo(12f, 18.9263f)
                curveTo(9.9241f, 18.9263f, 8.075f, 17.9631f, 6.871f, 16.4611f)
                curveTo(7.3964f, 14.1117f, 9.4924f, 12.3549f, 12f, 12.3549f)
                curveTo(14.5071f, 12.3549f, 16.6033f, 14.1117f, 17.1284f, 16.4611f)
                curveTo(15.9245f, 17.9631f, 14.0757f, 18.9263f, 12f, 18.9263f)
                close()
                moveTo(11.9826f, 5.8303f)
                curveTo(13.324f, 5.8303f, 14.4108f, 6.9174f, 14.4108f, 8.2586f)
                curveTo(14.4108f, 9.5997f, 13.324f, 10.6874f, 11.9826f, 10.6874f)
                curveTo(10.6409f, 10.6874f, 9.5538f, 9.5997f, 9.5538f, 8.2586f)
                curveTo(9.5538f, 6.9174f, 10.6409f, 5.8303f, 11.9826f, 5.8303f)
                close()
                moveTo(12.0003f, 2f)
                curveTo(6.477f, 2f, 2f, 6.4771f, 2f, 12f)
                curveTo(2f, 17.5231f, 6.477f, 22f, 12.0003f, 22f)
                curveTo(17.523f, 22f, 22f, 17.5231f, 22f, 12f)
                curveTo(22f, 6.4771f, 17.523f, 2f, 12.0003f, 2f)
                close()
            }
        }.build()

    /** Receipt / invoice icon */
    val Receipt: ImageVector
        get() = ImageVector.Builder(
            name = "Receipt",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).apply {
            path(
                stroke = SolidColor(Color.White),
                strokeLineWidth = 1.5f,
                strokeLineCap = StrokeCap.Round,
                strokeLineJoin = StrokeJoin.Round
            ) {
                moveTo(9f, 7.708f)
                horizontalLineTo(15f)
                moveTo(9f, 11.4755f)
                horizontalLineTo(15f)
                moveTo(9f, 15.2431f)
                horizontalLineTo(15f)
                moveTo(19.125f, 20.375f)
                lineTo(17.344f, 19.375f)
                lineTo(15.562f, 20.375f)
                lineTo(13.782f, 19.375f)
                lineTo(12f, 20.375f)
                lineTo(10.219f, 19.375f)
                lineTo(8.437f, 20.375f)
                lineTo(6.656f, 19.375f)
                lineTo(4.875f, 20.375f)
                verticalLineTo(6.125f)
                curveTo(4.875f, 4.744f, 5.994f, 3.625f, 7.375f, 3.625f)
                horizontalLineTo(16.625f)
                curveTo(18.006f, 3.625f, 19.125f, 4.744f, 19.125f, 6.125f)
                verticalLineTo(20.375f)
                close()
            }
        }.build()

    /** Settings gear icon */
    val Settings: ImageVector
        get() = ImageVector.Builder(
            name = "Settings",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).apply {
            path(
                fill = SolidColor(Color.White)
            ) {
                moveTo(20.1784f, 14.3f)
                lineTo(18.7784f, 12.9f)
                curveTo(18.5784f, 12.7f, 18.4784f, 12.4f, 18.4784f, 12.2f)
                curveTo(18.4784f, 12.2f, 18.4784f, 12.2f, 18.4784f, 12f)
                curveTo(18.4784f, 11.8f, 18.4784f, 11.8f, 18.4784f, 11.8f)
                curveTo(18.4784f, 11.6f, 18.5784f, 11.3f, 18.7784f, 11.1f)
                lineTo(20.1784f, 9.69998f)
                curveTo(20.3784f, 9.49998f, 20.3784f, 9.19998f, 20.2784f, 8.89998f)
                lineTo(18.7784f, 6.29998f)
                curveTo(18.6784f, 6.09998f, 18.2784f, 5.89998f, 18.0784f, 5.99998f)
                lineTo(16.1784f, 6.49998f)
                curveTo(15.8784f, 6.59998f, 15.5784f, 6.49998f, 15.3784f, 6.39998f)
                curveTo(15.1784f, 6.29998f, 14.6784f, 5.79998f, 14.5784f, 5.49998f)
                lineTo(14.0784f, 3.59998f)
                curveTo(13.9784f, 3.29998f, 13.7784f, 3.09998f, 13.4784f, 3.09998f)
                horizontalLineTo(10.4784f)
                curveTo(10.1784f, 3.09998f, 9.87836f, 3.29998f, 9.87836f, 3.59998f)
                lineTo(9.37836f, 5.49998f)
                curveTo(9.27836f, 5.79998f, 9.07836f, 6.09998f, 8.87836f, 6.19998f)
                curveTo(8.67836f, 6.29998f, 7.97836f, 6.49998f, 7.77836f, 6.49998f)
                lineTo(5.87836f, 5.99998f)
                curveTo(5.57836f, 5.89998f, 5.27836f, 6.09998f, 5.17836f, 6.29998f)
                lineTo(3.67836f, 8.89998f)
                curveTo(3.57836f, 9.09998f, 3.57836f, 9.49998f, 3.77836f, 9.69998f)
                lineTo(5.17836f, 11.1f)
                curveTo(5.37836f, 11.3f, 5.47836f, 11.6f, 5.47836f, 11.8f)
                curveTo(5.47836f, 11.8f, 5.47836f, 11.8f, 5.47836f, 12f)
                curveTo(5.47836f, 12.2f, 5.47836f, 12.2f, 5.47836f, 12.2f)
                curveTo(5.47836f, 12.4f, 5.37836f, 12.7f, 5.17836f, 12.9f)
                lineTo(3.77836f, 14.3f)
                curveTo(3.57836f, 14.5f, 3.57836f, 14.8f, 3.67836f, 15.1f)
                lineTo(5.17836f, 17.7f)
                curveTo(5.27836f, 17.9f, 5.67836f, 18.1f, 5.87836f, 18f)
                lineTo(7.77836f, 17.5f)
                curveTo(8.07836f, 17.4f, 8.37836f, 17.5f, 8.57836f, 17.6f)
                curveTo(8.77836f, 17.7f, 9.27836f, 18.2f, 9.37836f, 18.5f)
                lineTo(9.87836f, 20.4f)
                curveTo(9.97836f, 20.7f, 10.1784f, 20.9f, 10.4784f, 20.9f)
                horizontalLineTo(13.4784f)
                curveTo(13.7784f, 20.9f, 14.0784f, 20.7f, 14.0784f, 20.4f)
                lineTo(14.5784f, 18.5f)
                curveTo(14.6784f, 18.2f, 14.8784f, 17.9f, 15.0784f, 17.8f)
                curveTo(15.2784f, 17.7f, 15.9784f, 17.5f, 16.1784f, 17.5f)
                lineTo(18.0784f, 18f)
                curveTo(18.3784f, 18.1f, 18.6784f, 17.9f, 18.7784f, 17.7f)
                lineTo(20.2784f, 15.1f)
                curveTo(20.4784f, 14.9f, 20.3784f, 14.5f, 20.1784f, 14.3f)
                close()
                moveTo(12.0784f, 15f)
                curveTo(10.3784f, 15f, 9.07836f, 13.7f, 9.07836f, 12f)
                curveTo(9.07836f, 10.3f, 10.3784f, 8.99998f, 12.0784f, 8.99998f)
                curveTo(13.7784f, 8.99998f, 15.0784f, 10.3f, 15.0784f, 12f)
                curveTo(15.0784f, 13.7f, 13.6784f, 15f, 12.0784f, 15f)
                close()
            }
        }.build()

    /** Exchange / swap arrows icon */


    /** Back arrow (chevron left) */
    val Back: ImageVector
        get() = ImageVector.Builder(
            name = "Back",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).apply {
            path(
                stroke = SolidColor(Color.White),
                strokeLineWidth = 2f,
                strokeLineCap = StrokeCap.Round,
                strokeLineJoin = StrokeJoin.Round
            ) {
                moveTo(15f, 4f)
                lineTo(7f, 12f)
                lineTo(15f, 20f)
            }
        }.build()

    /** Close (X) icon */
    val Close: ImageVector
        get() = ImageVector.Builder(
            name = "Close",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).apply {
            path(
                stroke = SolidColor(Color.White),
                strokeLineWidth = 2f,
                strokeLineCap = StrokeCap.Round,
                strokeLineJoin = StrokeJoin.Round
            ) {
                moveTo(6f, 6f)
                lineTo(18f, 18f)
                moveTo(18f, 6f)
                lineTo(6f, 18f)
            }
        }.build()

    /** Check / tick icon */
    val Check: ImageVector
        get() = ImageVector.Builder(
            name = "Check",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).apply {
            path(
                stroke = SolidColor(Color.White),
                strokeLineWidth = 2f,
                strokeLineCap = StrokeCap.Round,
                strokeLineJoin = StrokeJoin.Round
            ) {
                moveTo(5f, 12f)
                lineTo(10f, 17f)
                lineTo(19f, 7f)
            }
        }.build()

    /** Search / magnifying glass icon */
    val Search: ImageVector
        get() = ImageVector.Builder(
            name = "Search",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).apply {
            path(
                stroke = SolidColor(Color.White),
                strokeLineWidth = 2f,
                strokeLineCap = StrokeCap.Round,
                strokeLineJoin = StrokeJoin.Round
            ) {
                // Circle using arcs (center=10,10 radius=6)
                moveTo(16f, 10f)
                arcTo(6f, 6f, 0f, isMoreThanHalf = true, isPositiveArc = true, 4f, 10f)
                arcTo(6f, 6f, 0f, isMoreThanHalf = true, isPositiveArc = true, 16f, 10f)
                close()
                // Handle
                moveTo(14.5f, 14.5f)
                lineTo(20f, 20f)
            }
        }.build()

    /** Filter / funnel icon */
    val Filter: ImageVector
        get() = ImageVector.Builder(
            name = "Filter",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).apply {
            path(
                stroke = SolidColor(Color.White),
                strokeLineWidth = 1.5f,
                strokeLineCap = StrokeCap.Round,
                strokeLineJoin = StrokeJoin.Round
            ) {
                moveTo(4f, 6f)
                horizontalLineTo(20f)
                moveTo(7f, 12f)
                horizontalLineTo(17f)
                moveTo(10f, 18f)
                horizontalLineTo(14f)
            }
        }.build()

    /** Apps / grid icon (4 rounded rectangles) */
    val Apps: ImageVector
        get() = ImageVector.Builder(
            name = "Apps",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).apply {
            path(
                fill = SolidColor(Color.White)
            ) {
                // Top-left
                moveTo(5f, 4f)
                horizontalLineTo(7f)
                arcTo(1f, 1f, 0f, isMoreThanHalf = false, isPositiveArc = true, 8f, 5f)
                verticalLineTo(7f)
                arcTo(1f, 1f, 0f, isMoreThanHalf = false, isPositiveArc = true, 7f, 8f)
                horizontalLineTo(5f)
                arcTo(1f, 1f, 0f, isMoreThanHalf = false, isPositiveArc = true, 4f, 7f)
                verticalLineTo(5f)
                arcTo(1f, 1f, 0f, isMoreThanHalf = false, isPositiveArc = true, 5f, 4f)
                close()
                // Top-right
                moveTo(17f, 4f)
                horizontalLineTo(19f)
                arcTo(1f, 1f, 0f, isMoreThanHalf = false, isPositiveArc = true, 20f, 5f)
                verticalLineTo(7f)
                arcTo(1f, 1f, 0f, isMoreThanHalf = false, isPositiveArc = true, 19f, 8f)
                horizontalLineTo(17f)
                arcTo(1f, 1f, 0f, isMoreThanHalf = false, isPositiveArc = true, 16f, 7f)
                verticalLineTo(5f)
                arcTo(1f, 1f, 0f, isMoreThanHalf = false, isPositiveArc = true, 17f, 4f)
                close()
                // Bottom-left
                moveTo(5f, 16f)
                horizontalLineTo(7f)
                arcTo(1f, 1f, 0f, isMoreThanHalf = false, isPositiveArc = true, 8f, 17f)
                verticalLineTo(19f)
                arcTo(1f, 1f, 0f, isMoreThanHalf = false, isPositiveArc = true, 7f, 20f)
                horizontalLineTo(5f)
                arcTo(1f, 1f, 0f, isMoreThanHalf = false, isPositiveArc = true, 4f, 19f)
                verticalLineTo(17f)
                arcTo(1f, 1f, 0f, isMoreThanHalf = false, isPositiveArc = true, 5f, 16f)
                close()
                // Bottom-right
                moveTo(17f, 16f)
                horizontalLineTo(19f)
                arcTo(1f, 1f, 0f, isMoreThanHalf = false, isPositiveArc = true, 20f, 17f)
                verticalLineTo(19f)
                arcTo(1f, 1f, 0f, isMoreThanHalf = false, isPositiveArc = true, 19f, 20f)
                horizontalLineTo(17f)
                arcTo(1f, 1f, 0f, isMoreThanHalf = false, isPositiveArc = true, 16f, 19f)
                verticalLineTo(17f)
                arcTo(1f, 1f, 0f, isMoreThanHalf = false, isPositiveArc = true, 17f, 16f)
                close()
            }
        }.build()

    /** ManageStorage / list icon */
    val ManageStorage: ImageVector
        get() = ImageVector.Builder(
            name = "ManageStorage",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).apply {
            path(
                stroke = SolidColor(Color.White),
                strokeLineWidth = 1.5f,
                strokeLineCap = StrokeCap.Round,
                strokeLineJoin = StrokeJoin.Round
            ) {
                // 3 horizontal bars
                moveTo(4f, 7f)
                horizontalLineTo(20f)
                moveTo(4f, 12f)
                horizontalLineTo(20f)
                moveTo(4f, 17f)
                horizontalLineTo(20f)
            }
        }.build()

    // ── Account type icons (Lucide) ──
    val Cash: ImageVector get() = Lucide.Wallet
    val Bank: ImageVector get() = Lucide.Building
    val DigitalWallet: ImageVector get() = Lucide.Wallet
    val CryptoExchange: ImageVector get() = Lucide.DollarSign

    // ── Feature icons (Lucide) ──
    val ExchangeRate: ImageVector get() = Lucide.CircleArrowUp
    val Changes: ImageVector
        get() = ImageVector.Builder(
            name = "CambioDeTasas",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).apply {
            path(
                stroke = SolidColor(Color.White),
                strokeLineWidth = 1.2f,
                strokeLineCap = StrokeCap.Round,
                strokeLineJoin = StrokeJoin.Round
            ) {
                moveTo(11.777f, 5.68161f)
                verticalLineTo(2.59961f)
                curveTo(11.777f, 1.49461f, 10.881f, 0.599609f, 9.77698f, 0.599609f)
                horizontalLineTo(3.09998f)
                curveTo(1.71898f, 0.599609f, 0.599976f, 1.71861f, 0.599976f, 3.09961f)
                verticalLineTo(9.77761f)
                curveTo(0.599976f, 10.8816f, 1.49498f, 11.7776f, 2.59998f, 11.7776f)
                horizontalLineTo(3.11898f)

                moveTo(5.92247f, 5.14941f)
                verticalLineTo(14.5994f)
                curveTo(5.92247f, 15.9804f, 7.04147f, 17.0994f, 8.42247f, 17.0994f)
                horizontalLineTo(14.5995f)
                curveTo(15.9805f, 17.0994f, 17.0995f, 15.9804f, 17.0995f, 14.5994f)
                verticalLineTo(8.95441f)
                curveTo(17.0995f, 7.84941f, 16.2045f, 6.95441f, 15.0995f, 6.95441f)
                horizontalLineTo(14.6725f)

                moveTo(3.60218f, 6.57521f)
                lineTo(5.56918f, 4.60821f)
                curveTo(5.76418f, 4.41321f, 6.08118f, 4.41321f, 6.27618f, 4.60821f)
                lineTo(8.24318f, 6.57521f)

                moveTo(14.0973f, 3.43551f)
                lineTo(12.1303f, 5.40251f)
                curveTo(11.9353f, 5.59651f, 11.6183f, 5.59651f, 11.4233f, 5.40251f)
                lineTo(9.45627f, 3.43551f)

                moveTo(9.55017f, 13.0277f)
                curveTo(9.56476f, 13.2931f, 9.62923f, 13.5284f, 9.74311f, 13.735f)
                curveTo(9.85746f, 13.9407f, 10.0123f, 14.1148f, 10.2085f, 14.2564f)
                curveTo(10.4048f, 14.3981f, 10.6349f, 14.5063f, 10.8979f, 14.5811f)
                curveTo(11.1619f, 14.6555f, 11.4499f, 14.6927f, 11.7633f, 14.6927f)
                curveTo(12.0386f, 14.6927f, 12.3045f, 14.6616f, 12.5601f, 14.5981f)
                curveTo(12.8161f, 14.535f, 13.0433f, 14.4404f, 13.241f, 14.3143f)
                curveTo(13.4386f, 14.1882f, 13.5968f, 14.0315f, 13.7153f, 13.8447f)
                curveTo(13.8339f, 13.6583f, 13.8932f, 13.44f, 13.8932f, 13.1901f)
                curveTo(13.8932f, 12.943f, 13.8396f, 12.7346f, 13.7328f, 12.5647f)
                curveTo(13.6259f, 12.3943f, 13.4824f, 12.2508f, 13.3026f, 12.1341f)
                curveTo(13.1224f, 12.0169f, 12.9158f, 11.919f, 12.6833f, 11.8404f)
                curveTo(12.4504f, 11.7619f, 10.9925f, 11.3383f, 10.7596f, 11.2597f)
                curveTo(10.5271f, 11.1811f, 10.321f, 11.0833f, 10.1408f, 10.9661f)
                curveTo(9.96052f, 10.8489f, 9.81699f, 10.7054f, 9.71064f, 10.5355f)
                curveTo(9.60335f, 10.3656f, 9.55017f, 10.1567f, 9.55017f, 9.90962f)
                curveTo(9.55017f, 9.6602f, 9.60946f, 9.44185f, 9.72805f, 9.25503f)
                curveTo(9.84664f, 9.0682f, 10.0048f, 8.91197f, 10.2024f, 8.78538f)
                curveTo(10.4001f, 8.65973f, 10.6269f, 8.56515f, 10.8829f, 8.50162f)
                curveTo(11.1389f, 8.43856f, 11.4048f, 8.4075f, 11.6796f, 8.4075f)
                curveTo(11.993f, 8.4075f, 12.2815f, 8.44468f, 12.545f, 8.51903f)
                curveTo(12.8085f, 8.59385f, 13.0382f, 8.70209f, 13.2344f, 8.84326f)
                curveTo(13.4306f, 8.98538f, 13.5855f, 9.15903f, 13.6998f, 9.36515f)
                curveTo(13.8142f, 9.57126f, 13.8782f, 9.80656f, 13.8932f, 10.072f)

                moveTo(11.7216f, 7.5498f)
                verticalLineTo(15.5498f)
            }
        }.build()

    val Savings: ImageVector get() = Lucide.PiggyBank
    val Budget: ImageVector get() = Lucide.DollarSign
    val Debt: ImageVector get() = Lucide.Users
    val Analytics: ImageVector get() = Lucide.ChartBar
    val AIChat: ImageVector get() = Lucide.Sparkles
    val ScanReceipt: ImageVector get() = Lucide.Camera
    val HandCoins: ImageVector get() = Lucide.HandCoins
    val Bell: ImageVector get() = Lucide.Bell
    val Target: ImageVector get() = Lucide.Target
    val Lock: ImageVector get() = Lucide.Lock
    val Unlock: ImageVector get() = Lucide.LockOpen
    val DarkMode: ImageVector get() = Lucide.Moon
    val LightMode: ImageVector get() = Lucide.Sun
    val Notifications: ImageVector get() = Lucide.Bell
    val Biometric: ImageVector get() = Lucide.Shield
    val Haptic: ImageVector get() = Lucide.Smartphone
    val Globe: ImageVector get() = Lucide.Globe
    val CurrencyIcon: ImageVector get() = Lucide.CircleDollarSign
    val Categories: ImageVector get() = Lucide.Palette
    val ExportData: ImageVector get() = Lucide.FileText
    val Help: ImageVector get() = Lucide.CircleHelp
    val Logout: ImageVector get() = Lucide.LogOut
    val ArrowIncome: ImageVector get() = Lucide.ArrowDownLeft
    val ArrowExpense: ImageVector get() = Lucide.ArrowUpRight
    val TrendingUp: ImageVector get() = Lucide.TrendingUp
    val TrendingDown: ImageVector get() = Lucide.TrendingDown
    val ChevronUp: ImageVector get() = Lucide.ChevronUp
    val ChevronDown: ImageVector get() = Lucide.ChevronDown

    // ── Category icons (OneUI @DrawableRes) ──
    val categoryIconMap: Map<String, Int> = mapOf(
        "Food" to IconsR.drawable.ic_oui_food,
        "Restaurant" to IconsR.drawable.ic_oui_restaurants,
        "Car" to IconsR.drawable.ic_oui_car,
        "Settings" to IconsR.drawable.ic_oui_settings,
        "Receipt" to IconsR.drawable.ic_oui_receipt,
        "Phone" to IconsR.drawable.ic_oui_phone,
        "Game" to IconsR.drawable.ic_oui_game,
        "Headphones" to IconsR.drawable.ic_oui_headphones,
        "Sports" to IconsR.drawable.ic_oui_sports,
        "Health" to IconsR.drawable.ic_oui_health,
        "Heart" to IconsR.drawable.ic_oui_heart,
        "Education" to IconsR.drawable.ic_oui_education,
        "School" to IconsR.drawable.ic_oui_school,
        "Home" to IconsR.drawable.ic_oui_home,
        "Shopping" to IconsR.drawable.ic_oui_shopping,
        "CreditCard" to IconsR.drawable.ic_oui_credit_card,
        "Dollar" to IconsR.drawable.ic_oui_symbol_dollar,
        "Dog" to IconsR.drawable.ic_oui_dog,
        "Gift" to IconsR.drawable.ic_oui_gift,
        "Calendar" to IconsR.drawable.ic_oui_calendar,
        "Alarm" to IconsR.drawable.ic_oui_alarm,
        "Star" to IconsR.drawable.ic_oui_star,
        "Tag" to IconsR.drawable.ic_oui_tag,
        "Save" to IconsR.drawable.ic_oui_save,
        "Pencil" to IconsR.drawable.ic_oui_pencil,
        "Close" to IconsR.drawable.ic_oui_close,
        "Delete" to IconsR.drawable.ic_oui_delete,
        "AddHome" to IconsR.drawable.ic_oui_add_home,
        "Flash" to IconsR.drawable.ic_oui_flashlight,
        "Wifi" to IconsR.drawable.ic_oui_wifi,
        "Search" to IconsR.drawable.ic_oui_search,
        "Apps" to IconsR.drawable.ic_oui_apps,
        "Animal" to IconsR.drawable.ic_oui_animal,
    )

    private val legacyIconNameMap: Map<String, String> = mapOf(
        "Restaurant" to "Restaurant",
        "DirectionsCar" to "Car",
        "Build" to "Settings",
        "PhoneAndroid" to "Phone",
        "SportsEsports" to "Game",
        "LocalHospital" to "Health",
        "School" to "School",
        "Home" to "Home",
        "Checkroom" to "Shopping",
        "Devices" to "Phone",
        "Fastfood" to "Food",
        "MoreHoriz" to "Apps",
        "Payments" to "CreditCard",
        "Work" to "Receipt",
        "SwapHoriz" to "Receipt",
        "AttachMoney" to "Dollar",
        "Category" to "Apps",
    )

    val allCategoryIcons: List<Pair<String, Int>> = categoryIconMap.entries.map { it.key to it.value }

    @DrawableRes
    fun getCategoryIconRes(iconName: String): Int {
        categoryIconMap[iconName]?.let { return it }
        val mappedName = legacyIconNameMap[iconName]
        if (mappedName != null) {
            categoryIconMap[mappedName]?.let { return it }
        }
        return IconsR.drawable.ic_oui_apps
    }
}
