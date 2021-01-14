package time.geekbang.org;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

@Setter
@Getter
@ToString
public class Book implements Serializable {
    private Integer id;
    private String name;
}
