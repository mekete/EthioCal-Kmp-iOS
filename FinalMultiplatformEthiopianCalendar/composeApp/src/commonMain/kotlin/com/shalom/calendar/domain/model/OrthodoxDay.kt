package com.shalom.calendar.domain.model

/**
 * Represents an Orthodox day name for each day of the month (1-30)
 * Each day has associated saints and religious observances
 */
data class OrthodoxDay(
    val geezDate: String,       // Geez numeral (፩, ፪, ፫, etc.)
    val ethiopianDate: String,  // Arabic numeral (1, 2, 3, etc.)
    val amharicName: String,    // Amharic name of saints/observances
    val englishName: String     // English name of saints/observances
)

object OrthodoxDaysList {
    /**
     * Returns the 30 Orthodox day names
     * Each day of the Ethiopian month (which has 30 days) has associated saints and observances
     */
    fun getOrthodoxDays(): List<OrthodoxDay> {
        return listOf(
            OrthodoxDay("፩", "1", "ልደታ\nራጉኤል\nኤልያስ", "Lideta (Birth of the Holy Virgin Mary) and Elias (Elijah)"),
            OrthodoxDay("፪", "2", "ታዴዎስ ሐዋርያ\nኢዮብ ጻድቅ", "Thaddius"),
            OrthodoxDay("፫", "3", "በዓታ ማርያም\nዜና ማርቆስ\nነአኩቶ ለአብ", "Be'eta (Presentation of the Holy Virgin to the Temple of Jerusalem)"),
            OrthodoxDay("፬", "4", "ዮሐንስ ወልደ ነጎድጓድ", "Yohannes Wolde Negedquad (John Son of Thunder)"),
            OrthodoxDay("፭", "5", "ጴጥሮስ ወጳውሎስ\nአቡነ ገብረ መንፈስ ቅዱስ", "Petros and Paulos (Peter and Paul) and Gebre Menfes Kiddus"),
            OrthodoxDay("፮", "6", "ኢየሱስ\nቁስቋም\nአርሴማ ቅድስት", "Our Lady of Qusquam (Egypt)"),
            OrthodoxDay("፯", "7", "ሥላሴ\nፊሊሞን\nአብላንዮስ", "Holy Trinity Day"),
            OrthodoxDay("፰", "8", "ማቴዎስ\nዮልያኖስ\nአባ ኪሮስ", "Kiros (Cyrus) and Abba Banuda"),
            OrthodoxDay("፱", "9", "ቶማስ ሐዋርያ\nእንድርያስ ሐዋርያ\nአውሳብዮስ\nአርባ ሰማዕታት", "Thomas (not the Apostle)"),
            OrthodoxDay("፲", "10", "በዓለ መስቀሉ ለእግዚእነ", "Kidus Meskel (Feast of the Holy Cross)"),
            OrthodoxDay("፲፩", "11", "ሃና ወኢያቄም\nቅዱስ ፋሲለደስ ሰማዕት", "Hanna we Iyachew (St Anne and St. Joachim, parents of the Holy Virgin Mary) and Fasilides"),
            OrthodoxDay("፲፪", "12", "ቅዱስ ሚካኤል\nክርስቶስ ሠምራ", "Michael the Archangel, Samuel, and Yared"),
            OrthodoxDay("፲፫", "13", "እግዚአብሔር አብ\nቅዱስ ሩፋኤል ሊቀ መላእክት", "Feast of Igziabher Ab (God the Father) and Raphael the Archangel"),
            OrthodoxDay("፲፬", "14", "አባ አረጋዊ\nአባ ገብረ ክርስቶስ\nድምጥያኖስ ሰማዕት", "Abuna Aregawi and Gebre Kristos"),
            OrthodoxDay("፲፭", "15", "ቂርቆስና ኢየሉጣ\nስልፋኮስ", "Kirkos and his mother Iyeluta (Cyricus and Julitta)"),
            OrthodoxDay("፲፮", "16", "ኪዳነ ምሕረት\nሚካኤል ጳጳስ", "Kidane Mihret (Our Lady Covenant of Mercy)"),
            OrthodoxDay("፲፯", "17", "ቅዱስ እስጢፋኖስ\nሉቃስ ዘዓምደ ብርሃን", "Estifanos (Stephen the Martyr) and Abba Gerima"),
            OrthodoxDay("፲፰", "18", "ፊልጶስ ሐዋርያ\nኤስድሮስ ሰማዕት\nኤዎስጣጤዎስ ሰማዕት", "Ewostatewos"),
            OrthodoxDay("፲፱", "19", "ቅዱስ ገብርኤል\nአርቃዲዎስ", "Gabriel the Archangel"),
            OrthodoxDay("፳", "20", "ጽንሰታ ለማርያም\nነቢዩ ኤልሳ\nሐጌ ነቢይ\nአባ ሰላማ መተርጉም", "Hnstata"),
            OrthodoxDay("፳፩", "21", "በዓለ እግዝእትነ ማርያም", "Holy Virgin Mary, Mother of God"),
            OrthodoxDay("፳፪", "22", "ቅዱስ ዑራኤል\nያዕቆብ ምሥራቃዊ\nደቅስዮስ", "Deqsius"),
            OrthodoxDay("፳፫", "23", "ቅዱስ ጊዮርጊስ\nለጊኖስ ሰማዕት", "St George"),
            OrthodoxDay("፳፬", "24", "አቡነ ተክለ ሃይማኖት", "Abune Tekle Haymanot"),
            OrthodoxDay("፳፭", "25", "መርቆሬዎስ\nአኒፍኖስ", "Merkorios (Saint Mercurius)"),
            OrthodoxDay("፳፮", "26", "ሆሴዕ ነቢይ\nሳዶቅ ሰማዕት", "Thomas the Apostle"),
            OrthodoxDay("፳፯", "27", "መድኃኔዓለም\nሕዝቅያስ ነቢይ\nአባ ዮሐንስ", "Medhane Alem (Savior of the World)"),
            OrthodoxDay("፳፰", "28", "አማኑኤል\nቆስጠንጢኖስ\nአብርሃም", "Immanuel"),
            OrthodoxDay("፳፱", "29", "በዓለ ወልድ\nሳሙኤል ዘወገግ", "Bale Wold (Feast of God the Son)"),
            OrthodoxDay("፴", "30", "ማርቆስ ወንጌላዊ", "Markos (Mark the Evangelist)")
        )
    }

    /**
     * Get Orthodox day by day number (1-30)
     */
    fun getForDay(dayNumber: Int): OrthodoxDay? {
        return if (dayNumber in 1..30) {
            getOrthodoxDays()[dayNumber - 1]
        } else {
            null
        }
    }
}
