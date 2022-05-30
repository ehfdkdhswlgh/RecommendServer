package persistence.DTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommentsDTO {

    private int commentsNumber;
    private int memberNumber;
    private int foodNum;
    private String content;

    private String memberID;
    private String foodName;

    public CommentsDTO(){}

    public CommentsDTO(int memberNumber, int foodNum, String content){
        this.memberNumber = memberNumber;
        this.foodNum = foodNum;
        this.content = content;
    }

    public CommentsDTO(int commentsNumber, int memberNumber, int foodNum, String content, int likeCount, int hateCount){
        this.commentsNumber = commentsNumber;
        this.memberNumber = memberNumber;
        this.foodNum = foodNum;
        this.content = content;
    }

    public String toString(){
        return memberID + " : " + content;
    }

}
