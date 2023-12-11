package help.me.aiQuiz.models;

import com.fasterxml.jackson.annotation.JsonAlias;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class Option {
    @Id
    @GeneratedValue
    private long id;
    private String caption;
    @JsonAlias("isAnswer")
    private boolean isAnswer;
}
