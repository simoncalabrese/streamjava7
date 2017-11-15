/**
 * Created by simon.calabrese on 14/11/2017.
 */
public class TestPair {
    private String name;
    private Integer age;

    public TestPair(String name, Integer age) {
        this.name = name;
        this.age = age;
    }

    public TestPair(Integer age) {
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }
}
