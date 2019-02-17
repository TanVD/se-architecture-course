package tanvd.gel.utils

fun String.trim(vararg char: Char?): String {
    val charsNotNull = char.filterNotNull()
    return if (charsNotNull.isEmpty()) {
        this
    } else {
        (this as CharSequence).trim { it in charsNotNull }.toString()
    }
}
