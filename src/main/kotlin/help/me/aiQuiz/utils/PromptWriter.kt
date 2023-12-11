package help.me.aiQuiz.utils

object PromptWriter {
    fun forQuiz(topic: String, complexity: String, questionsCount: Int, additionalInfo: String): String {
        return """
                Generate quiz, for given topic, provide only json code

                Option have this structure:
                    {
                        caption // string
                        answer // true if this is correct answer, otherwise false
                    }

                Question have this structure:
                    {
                        caption // string
                        options[] // array of options
                    }

                Quiz must have this structure:
                    {
                        name // string
                        questions[] // array of questions
                    }

                quiz topic: $topic  
                complexity: $complexity  
                questions count: $questionsCount   

               $additionalInfo  
            """
    }

}