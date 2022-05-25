import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Handler {
    private List<Person> personList;

    public Handler() {
        this.personList = new ArrayList<>();
        personList.add(new Person("Bob", "Smitt",  LocalDate.of(1914, 7, 28)));

    }

    public String getAllPerson(){
        return personList.toString();
    }

    public void delete() {
        //
    }

    public void update() {
    }

    public void insert() {
    }
}
