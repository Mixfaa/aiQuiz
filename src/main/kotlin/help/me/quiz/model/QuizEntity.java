package help.me.quiz.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@EqualsAndHashCode(callSuper = true)
@Data
@Document("help.me.quiz")
@NoArgsConstructor
public class QuizEntity extends Quiz {
    @Id
    private ObjectId id;

    public QuizEntity(Quiz quiz) {
        super(quiz.getName(), quiz.getTopic(), quiz.getSubject(), quiz.getQuestions(), quiz.getAdditionalInfo());
    }
}
