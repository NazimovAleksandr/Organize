package com.core.ui.composable

import androidx.compose.foundation.interaction.InteractionSource
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.selection.LocalTextSelectionColors
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.takeOrElse
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BasicTextFieldWithPlaceholder(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    readOnly: Boolean = false,
    textStyle: TextStyle = TextStyle.Default,
    label: @Composable (() -> Unit)? = null,
    placeholder: @Composable (() -> Unit)? = null,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    prefix: @Composable (() -> Unit)? = null,
    suffix: @Composable (() -> Unit)? = null,
    supportingText: @Composable (() -> Unit)? = null,
    isError: Boolean = false,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardOptions: KeyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Sentences),
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    singleLine: Boolean = false,
    maxLines: Int = if (singleLine) 1 else 6,
    minLines: Int = 1,
    onTextLayout: (TextLayoutResult) -> Unit = {},
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    cursorBrush: Brush = SolidColor(Color.Black),
    shape: Shape = RoundedCornerShape(size = 0.dp),
    colors: BasicTextFieldColors = BasicTextFieldColors(),
    contentPadding: PaddingValues = PaddingValues(all = 0.dp),
    container: @Composable () -> Unit = {
        TextFieldDefaults.ContainerBox(enabled, isError, interactionSource, colors.toTextFieldColors(), shape)
    },
) {
    val textColor = textStyle.color.takeOrElse {
        textColor(
            enabled,
            isError,
            interactionSource,
            colors.disabledTextColor,
            colors.errorTextColor,
            colors.focusedTextColor,
            colors.unfocusedTextColor,
        ).value
    }

    val mergedTextStyle = textStyle.merge(TextStyle(color = textColor))

    CompositionLocalProvider(
        LocalTextSelectionColors provides TextSelectionColors(
            handleColor = MaterialTheme.colorScheme.surface,
            backgroundColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.5f),
        )
    ) {
        BasicTextField(
            value = TextFieldValue(text = value, selection = TextRange(value.length)),
            onValueChange = { onValueChange.invoke(it.text) },
            modifier = modifier,
            enabled = enabled,
            readOnly = readOnly,
            textStyle = mergedTextStyle,
            keyboardOptions = keyboardOptions,
            keyboardActions = keyboardActions,
            singleLine = singleLine,
            maxLines = maxLines,
            minLines = minLines,
            visualTransformation = visualTransformation,
            onTextLayout = onTextLayout,
            interactionSource = interactionSource,
            cursorBrush = cursorBrush,
            decorationBox = @Composable { innerTextField ->
                TextFieldDefaults.DecorationBox(
                    value = value,
                    innerTextField = innerTextField,
                    enabled = enabled,
                    singleLine = singleLine,
                    visualTransformation = visualTransformation,
                    interactionSource = interactionSource,
                    isError = isError,
                    label = label,
                    placeholder = placeholder,
                    leadingIcon = leadingIcon,
                    trailingIcon = trailingIcon,
                    prefix = prefix,
                    suffix = suffix,
                    supportingText = supportingText,
                    shape = shape,
                    colors = colors.toTextFieldColors(),
                    contentPadding = contentPadding,
                    container = container
                )
            },
        )
    }
}

@Composable
private fun textColor(
    enabled: Boolean,
    isError: Boolean,
    interactionSource: InteractionSource,
    disabledTextColor: Color,
    errorTextColor: Color,
    focusedTextColor: Color,
    unfocusedTextColor: Color,
): State<Color> {
    val focused by interactionSource.collectIsFocusedAsState()

    val targetValue = when {
        !enabled -> disabledTextColor
        isError -> errorTextColor
        focused -> focusedTextColor
        else -> unfocusedTextColor
    }
    return rememberUpdatedState(targetValue)
}

@Suppress("MemberVisibilityCanBePrivate")
class BasicTextFieldColors constructor(
    val focusedTextColor:Color = Color.Black,
    val unfocusedTextColor:Color = Color.Black,
    val disabledTextColor:Color = Color.Gray,
    val errorTextColor:Color = Color.Red,
    val focusedContainerColor:Color = Color.Transparent,
    val unfocusedContainerColor:Color = Color.Transparent,
    val disabledContainerColor:Color = Color.Transparent,
    val errorContainerColor:Color = Color.Transparent,
    val cursorColor:Color = Color.Black,
    val errorCursorColor:Color = Color.Red,
    val focusedIndicatorColor:Color = Color.Transparent,
    val unfocusedIndicatorColor:Color = Color.Transparent,
    val disabledIndicatorColor:Color = Color.Gray,
    val errorIndicatorColor:Color = Color.Black,
    val focusedLeadingIconColor:Color = Color.Black,
    val unfocusedLeadingIconColor:Color = Color.Black,
    val disabledLeadingIconColor:Color = Color.Gray,
    val errorLeadingIconColor:Color = Color.Red,
    val focusedTrailingIconColor:Color = Color.Black,
    val unfocusedTrailingIconColor:Color = Color.Black,
    val disabledTrailingIconColor:Color = Color.Gray,
    val errorTrailingIconColor:Color = Color.Red,
    val focusedLabelColor:Color = Color.Black,
    val unfocusedLabelColor:Color = Color.Black,
    val disabledLabelColor:Color = Color.Gray,
    val errorLabelColor:Color = Color.Red,
    val focusedPlaceholderColor:Color = Color.Black,
    val unfocusedPlaceholderColor:Color = Color.Black,
    val disabledPlaceholderColor:Color = Color.Gray,
    val errorPlaceholderColor:Color = Color.Red,
    val focusedSupportingTextColor:Color = Color.Black,
    val unfocusedSupportingTextColor:Color = Color.Black,
    val disabledSupportingTextColor:Color = Color.Gray,
    val errorSupportingTextColor:Color = Color.Red,
    val focusedPrefixColor:Color = Color.Black,
    val unfocusedPrefixColor:Color = Color.Black,
    val disabledPrefixColor:Color = Color.Gray,
    val errorPrefixColor:Color = Color.Red,
    val focusedSuffixColor:Color = Color.Black,
    val unfocusedSuffixColor:Color = Color.Black,
    val disabledSuffixColor:Color = Color.Gray,
    val errorSuffixColor:Color = Color.Red,
) {
    @Composable
    fun toTextFieldColors() =  TextFieldDefaults.colors(
        focusedTextColor = focusedTextColor,
        unfocusedTextColor = unfocusedTextColor,
        disabledTextColor = disabledTextColor,
        errorTextColor = errorTextColor,
        focusedContainerColor = focusedContainerColor,
        unfocusedContainerColor = unfocusedContainerColor,
        disabledContainerColor = disabledContainerColor,
        errorContainerColor = errorContainerColor,
        cursorColor = cursorColor,
        errorCursorColor = errorCursorColor,
        selectionColors = LocalTextSelectionColors.current,
        focusedIndicatorColor = focusedIndicatorColor,
        unfocusedIndicatorColor = unfocusedIndicatorColor,
        disabledIndicatorColor = disabledIndicatorColor,
        errorIndicatorColor = errorIndicatorColor,
        focusedLeadingIconColor = focusedLeadingIconColor,
        unfocusedLeadingIconColor = unfocusedLeadingIconColor,
        disabledLeadingIconColor = disabledLeadingIconColor,
        errorLeadingIconColor = errorLeadingIconColor,
        focusedTrailingIconColor = focusedTrailingIconColor,
        unfocusedTrailingIconColor = unfocusedTrailingIconColor,
        disabledTrailingIconColor = disabledTrailingIconColor,
        errorTrailingIconColor = errorTrailingIconColor,
        focusedLabelColor = focusedLabelColor,
        unfocusedLabelColor = unfocusedLabelColor,
        disabledLabelColor = disabledLabelColor,
        errorLabelColor = errorLabelColor,
        focusedPlaceholderColor = focusedPlaceholderColor,
        unfocusedPlaceholderColor = unfocusedPlaceholderColor,
        disabledPlaceholderColor = disabledPlaceholderColor,
        errorPlaceholderColor = errorPlaceholderColor,
        focusedSupportingTextColor = focusedSupportingTextColor,
        unfocusedSupportingTextColor = unfocusedSupportingTextColor,
        disabledSupportingTextColor = disabledSupportingTextColor,
        errorSupportingTextColor = errorSupportingTextColor,
        focusedPrefixColor = focusedPrefixColor,
        unfocusedPrefixColor = unfocusedPrefixColor,
        disabledPrefixColor = disabledPrefixColor,
        errorPrefixColor = errorPrefixColor,
        focusedSuffixColor = focusedSuffixColor,
        unfocusedSuffixColor = unfocusedSuffixColor,
        disabledSuffixColor = disabledSuffixColor,
        errorSuffixColor = errorSuffixColor,
    )
}

@Preview
@Composable
private fun Preview() {
    Box(
        contentAlignment = Alignment.TopStart,
        modifier = Modifier
//            .background(color = Color.Red)
            .padding(all = 20.dp)
    ) {
        BasicTextFieldWithPlaceholder(
            value = "text",
            onValueChange = {}
        )
    }
}