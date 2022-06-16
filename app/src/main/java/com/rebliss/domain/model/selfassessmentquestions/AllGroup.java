package com.rebliss.domain.model.selfassessmentquestions;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AllGroup {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("question")
    @Expose
    private String question;
    @SerializedName("question_type")
    @Expose
    private int questionType;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("multichoiceOptions")
    @Expose
    private List<MultichoiceOption> multichoiceOptions = null;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public int getQuestionType() {
        return questionType;
    }

    public void setQuestionType(int questionType) {
        this.questionType = questionType;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public List<MultichoiceOption> getMultichoiceOptions() {
        return multichoiceOptions;
    }

    public void setMultichoiceOptions(List<MultichoiceOption> multichoiceOptions) {
        this.multichoiceOptions = multichoiceOptions;
    }

}
