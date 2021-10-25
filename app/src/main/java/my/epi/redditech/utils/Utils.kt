package my.epi.redditech.utils

class Utils {
    companion object {
        fun getFormatNumber(number: Int) : String {
            var str = number.toString()

            if (str.length > 6) {
                str = str.substring(0, str.length - 6) + 'M'
            } else if (str.length > 3) {
                str = str.substring(0, str.length - 3) + 'K'
            }
            return str
        }
    }
}