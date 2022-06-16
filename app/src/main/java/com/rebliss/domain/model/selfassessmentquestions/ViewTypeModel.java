package com.rebliss.domain.model.selfassessmentquestions;

public class ViewTypeModel {

    public static final int QUESTION_TYPE_SINGLE_LINE = 1;
    public static final int QUESTION_TYPE_MULTIPLE_LINE = 2;
    public static final int QUESTION_TYPE_MULTIPLE_CHOICE = 3;

    public int type;
    public int data;
    public String text;

    public ViewTypeModel(int type, String text)
    {
        this.type=type;
        this.text=text;

    }

}
