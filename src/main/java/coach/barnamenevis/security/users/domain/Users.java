package coach.barnamenevis.security.users.domain;

import coach.barnamenevis.security.enums.UserRoles;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
public class Users implements Serializable {

    @Id
    @GeneratedValue
    private Long id;

    private String email;
    private String password;

    private Boolean enabled = true;

    @ElementCollection(targetClass = UserRoles.class)
    @CollectionTable(
            name = "authorities",
            joinColumns = @JoinColumn(name = "email",referencedColumnName = "email"))
    @Enumerated(EnumType.STRING)
    private List<UserRoles> userRoles;

    public Users() {
    }

    public Users(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public List<UserRoles> getUserRoles() {
        return userRoles;
    }

    public void setUserRoles(List<UserRoles> userRoles) {
        this.userRoles = userRoles;
    }
}
