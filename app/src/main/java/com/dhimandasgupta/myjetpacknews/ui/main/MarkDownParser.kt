package com.dhimandasgupta.myjetpacknews.ui.main

import android.content.Context
import android.webkit.WebView
import androidx.compose.Composable
import androidx.compose.state
import androidx.ui.core.ContextAmbient
import androidx.ui.core.Modifier
import androidx.ui.foundation.TextField
import androidx.ui.foundation.currentTextStyle
import androidx.ui.graphics.Color
import androidx.ui.input.OffsetMap
import androidx.ui.input.TextFieldValue
import androidx.ui.input.TransformedText
import androidx.ui.input.VisualTransformation
import androidx.ui.layout.Column
import androidx.ui.layout.fillMaxWidth
import androidx.ui.material.Divider
import androidx.ui.text.AnnotatedString
import androidx.ui.text.SpanStyle
import androidx.ui.text.font.FontFamily
import androidx.ui.tooling.preview.Preview
import androidx.ui.unit.sp
import androidx.ui.viewinterop.AndroidView

@Preview(showDecoration = true)
@Composable
fun MarkDownParser() {
    val mdRaw = """
   <ol><li>Las Cruces man's death at hands of police ruled a homicide  Las Cruces Sun-News\r\n</li><li>New Mexico cop faces manslaughter charge in death of man he placed in neck restraint: prosecutor  Fox News\r\n</li><li>New Mexico cop charged with involuntary mans…
   """.trimIndent()

    val ctx = ContextAmbient.current

    val webView = webview(ctx)

    val (md, setMd) = state { TextFieldValue(mdRaw) }
    val (html, setHtml) = state { TextFieldValue(parseMd(mdRaw)) }

    webView.loadData(html.text, "text/html", "utf-8")

    Column {
        CodeEditor(
            value = md,
            onValueChange = { setMd(it);setHtml(TextFieldValue(parseMd(it.text))) })
        Divider()
        CodeEditor(value = html, onValueChange = { setHtml(it) })
        Divider()
        AndroidView(view = webView)
    }
}

@Composable
fun CodeEditor(value: TextFieldValue, onValueChange: (TextFieldValue) -> Unit) {
    TextField(modifier = Modifier.fillMaxWidth(),
        onTextLayout = {
        },
        textStyle = currentTextStyle().copy(
            fontFamily = FontFamily.Monospace,
            fontSize = 16.sp
        ),
        value = value,
        onValueChange = onValueChange,
        visualTransformation = CodeVisualTransformation()
    )
}

class CodeVisualTransformation : VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText {

        val transformedText = text.text
            .split("\n")
            .mapIndexed { i, t -> String.format("%2d %s", i + 1, t) }
            .joinToString("\n")

        val gutterSize = 3 // "%2d "

        val codeOffsetTranslator = object : OffsetMap {
            override fun originalToTransformed(offset: Int): Int =
                gutterSize + offset + text.text.take(offset).count { it == '\n' } * gutterSize

            override fun transformedToOriginal(offset: Int): Int =
                offset - gutterSize - transformedText.take(offset).count { it == '\n' } * gutterSize
        }

        val lineNumStyle = SpanStyle(color = Color.LightGray, fontSize = 12.sp)

        val transformedTextStyled = with(AnnotatedString.Builder(transformedText)) {

            """^.{2} """.toRegex(RegexOption.MULTILINE).findAll(transformedText).forEach {
                addStyle(lineNumStyle, it.range.first, it.range.last)
            }

            toAnnotatedString()
        }

        return TransformedText(transformedTextStyled, codeOffsetTranslator)
    }
}

fun webview(ctx: Context) = WebView(ctx)

fun parseMd(md: String): String =
    md.replace("""^\s*\n\*""".toRegex(), "<ul>\n*")
        .replace("""^(\*.+)\s*\n([^*])""".toRegex(), "$1\n</ul>\n\n$2")
        .replace("""^\*(.+)""".toRegex(), "<li>$1</li>")

        //ol
        .replace("""^\s*\n\d\.""".toRegex(), "<ol>\n1.")
        .replace("""^(\d\..+)\s*\n([^\d.])""".toRegex(), "$1\n</ol>\n\n$2")
        .replace("""^\d\.(.+)""".toRegex(), "<li>$1</li>")

        //blockquote
        .replace("""^>(.+)""".toRegex(), "<blockquote>$1</blockquote>")

        //h
        .replace("""#{6} (.+)""".toRegex(), "<h6>$1</h6>")
        .replace("""#{5} (.+)""".toRegex(), "<h5>$1</h5>")
        .replace("""#{4} (.+)""".toRegex(), "<h4>$1</h4>")
        .replace("""#{3} (.+)""".toRegex(), "<h3>$1</h3>")
        .replace("""#{2} (.+)""".toRegex(), "<h2>$1</h2>")
        .replace("""# (.+)""".toRegex(), "<h1>$1</h1>")

        //alt h
        .replace("""^(.+)\n={3}""".toRegex(), "<h1>$1</h1>")
        .replace("""^(.+)\n-{3}""".toRegex(), "<h2>$1</h2>")

        //images
        .replace("""!\[([^]]+)]\(([^)]+)\)""".toRegex(), """<img src="$2" alt="$1" />""")

        //links
        .replace(
            """[\[]([^]]+)[]][(]([^)"]+)("(.+)")?[)]""".toRegex(),
            """<a href="$2" title="$4">$1</a>"""
        )

        //font styles
        .replace("""[*_]{2}([^*_]+)[*_]{2}""".toRegex(), "<b>$1</b>")
        .replace("""[*_]([^*_]+)[*_]""".toRegex(), "<i>$1</i>")
        .replace("""[~]{2}([^~]+)[~]{2}""".toRegex(), "<del>$1</del>")

        //pre
        .replace("""^\s*\n```([^\s]+)?""".toRegex(), """<pre class="$2">""")
        .replace("""^```\s*\n""".toRegex(), "</pre>\n\n")

        //code
        .replace("""`([^`]+)`""".toRegex(), "<code>$1</code>")

        //p
        .replace("""^\s*(\n)?(.+)""".toRegex()) { m ->
            if ("""</?(h\d|ul|ol|li|blockquote|pre|img)""".toRegex()
                    .containsMatchIn(m.value)
            ) m.value else "<p>${m.value}</p>"
        }

        //strip p from pre
        .replace("""(<pre.+>)\s*\n<p>(.+)</p>""".toRegex(), "$1$2")