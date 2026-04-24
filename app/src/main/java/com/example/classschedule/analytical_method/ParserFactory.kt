package com.example.classschedule.analytical_method

object ParserFactory {
    private val parsers = listOf(
        DefaultSchoolParser(),
    )


    fun getParser(schoolName: String): CourseParser {
        return parsers.find { it.schoolName == schoolName }
            ?: DefaultSchoolParser()
    }

}