package al.kr.mdfactory.models;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import al.kr.mdfactory.models.util.ManyToMany;

public final class Employee implements Comparable<Employee> {

    @NonNull private String login;
    private String name;
    private String password;
    @ManyToMany
    private List<OperationGroup> operationGroups = new ArrayList<>();

    public Employee(@NonNull String login) {
        this.login = login;
    }

    public void addOperationGroup(OperationGroup operationGroup) {
        operationGroups.add(operationGroup);
    }

    public void removeOperationGroup(OperationGroup operationGroup) {
        operationGroups.remove(operationGroup);
    }

    @NonNull public String getLogin() {
        return login;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    public void setLogin(@NonNull String login) {
        this.login = login;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<OperationGroup> getOperationGroups() {
        return operationGroups;
    }

    public void setOperationGroups(List<OperationGroup> operationGroups) {
        this.operationGroups = operationGroups;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Employee employee = (Employee) o;
        return login.equals(employee.login);
    }

    @Override
    public int hashCode() {
        return Objects.hash(login);
    }

    @Override
    public int compareTo(Employee o) {
        return name.compareTo(o.getName());
    }
}
