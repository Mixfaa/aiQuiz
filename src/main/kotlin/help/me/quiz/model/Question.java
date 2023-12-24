package help.me.quiz.model;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@Document("question")
public class Question {
    @Id
    @GeneratedValue
    private ObjectId id;

    private String caption;
    private List<Option> options;

    public boolean haveFewAnswers() {
        return options.stream().filter(Option::isAnswer).count() > 1;
    }
}


/*

@Entity
@Data
public class Question {
    @Id
    @GeneratedValue
    private long id;

    private String caption;
    @OneToMany(cascade = CascadeType.ALL)
    private List<Option> options;

    public boolean haveFewAnswers() {
        return options.stream().filter(Option::isAnswer).count() > 1;
    }
}
 */

