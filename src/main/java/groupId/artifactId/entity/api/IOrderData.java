package groupId.artifactId.entity.api;

import java.util.List;

public interface IOrderData {
    IToken getToken();

    List<IOrderStage> getHistory();

    boolean isDone();
}
