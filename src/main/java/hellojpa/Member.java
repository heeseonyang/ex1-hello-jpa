package hellojpa;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

@Entity
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MEMBER_ID")
    private Long id;

    @Column(name = "USERNAME")
    private String username;

  /*  @Column(name="TEAM_ID")
    private Long teamId;*/

    @ManyToOne //멤버입장에서는 매니고 팀 입장에서는 원이다.
    @JoinColumn(name="TEAM_ID")
    private Team team; /*jpa에 누가 다 고 누가 1인지 알려주어야한다*/


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Team getTeam() {
        return team;
    }

    public void setTeam(Team team) {
        this.team = team;
    }
}

