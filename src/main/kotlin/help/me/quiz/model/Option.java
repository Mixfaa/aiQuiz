package help.me.quiz.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document("option")
public class Option {
    @Id
    private ObjectId id;
    private String caption;
    @JsonAlias("isAnswer")
    private boolean isAnswer;
}


/*


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
 */