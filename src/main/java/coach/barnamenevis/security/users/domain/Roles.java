package coach.barnamenevis.security.users.domain;

import coach.barnamenevis.security.enums.Authority;

import javax.persistence.*;
import java.util.List;

@Entity
public class Roles {

    @Id
    @GeneratedValue
    private Long id;

    private String name;

    @ElementCollection(targetClass = Authority.class, fetch = FetchType.EAGER)
    private List<Authority> authorities;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Authority> getAuthorities() {
        return authorities;
    }

    public void setAuthorities(List<Authority> authorities) {
        this.authorities = authorities;
    }
}
