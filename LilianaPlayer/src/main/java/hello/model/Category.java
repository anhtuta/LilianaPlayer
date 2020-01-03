package hello.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @author tu.ta1 on 2019-05-06
 */
@Getter
@Setter
public class Category {

    private String name;
    private List<String> fileList;
}
