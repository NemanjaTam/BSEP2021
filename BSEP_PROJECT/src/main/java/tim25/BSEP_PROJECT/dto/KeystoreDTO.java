package tim25.BSEP_PROJECT.dto;

import java.util.ArrayList;
import java.util.List;

public class KeystoreDTO {
    List<Long> idList = new ArrayList<Long>();
    String name;
    String password;

    public KeystoreDTO() {
    }

    public List<Long> getIdList() {
        return idList;
    }

    public void setIdList(List<Long> idList) {
        this.idList = idList;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
}
