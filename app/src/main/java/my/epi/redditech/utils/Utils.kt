package my.epi.redditech.utils

import android.util.Log
import java.sql.Timestamp
import java.util.*

class Utils {
    companion object {
        /**
         * Format a big number - add a suffix (M million or K ...)
         */
        fun getFormatNumber(number: Int) : String {
            var str = number.toString()

            if (str.length > 6) {
                str = str.substring(0, str.length - 6) + 'M'
            } else if (str.length > 3) {
                str = str.substring(0, str.length - 3) + 'k'
            }
            return str
        }

        private fun formatNumber(number: String) : String {
            if (number.length == 1) {
                return "0$number";
            } else {
                return number
            }
        }

        fun formatDate(dateUtc : Long): String {
            val stamp = Timestamp(dateUtc * 1000)
            val date = Date(stamp.time)
            var day = formatNumber(date.date.toString())
            var month = formatNumber((date.month + 1).toString())
            var year = (date.year + 1900).toString()
            var minutes = formatNumber(date.minutes.toString())
            var hours = date.hours.toString()

            return "$day/$month/$year $hours:$minutes";
        }
    }
}