package help.me.quiz.model;

import jakarta.persistence.GeneratedValue;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import java.io.Serializable;

@RedisHash("quiz")
public class QuizCached extends Quiz implements Serializable {
    @Id
    @GeneratedValue
    private long id;

    public QuizCached(Quiz quiz) {
        super(quiz.getName(), quiz.getTopic(), quiz.getSubject(), quiz.getQuestions(), quiz.getAdditionalInfo());
    }
}
