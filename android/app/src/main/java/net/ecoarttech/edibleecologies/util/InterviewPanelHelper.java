package net.ecoarttech.edibleecologies.util;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import net.ecoarttech.edibleecologies.R;

import java.util.List;

/**
 * Created by pkoronkevich on 4/10/15.
 */
public class InterviewPanelHelper {
    private LayoutInflater mInflater;

    private LinearLayout mInterviewPanel;

    private List<String> mQuestions;
    private boolean mPanelOpen = false;
    private boolean mQuestionsPopulated = false;

    public InterviewPanelHelper(LayoutInflater inflater, LinearLayout panel){
        mInflater = inflater;
        mInterviewPanel = panel;
    }

    public void toggleInterviewPanel(){
        // TODO - add animation
        if (!mPanelOpen){
            showInterviewQuestions();
        }
        else{
            hideInterviewQuestions();
        }
        mPanelOpen = !mPanelOpen;
    }

    private void showInterviewQuestions(){
        if (mQuestions == null){
            // TODO - display error message
        }
        else{
            if (mQuestionsPopulated){
                // just reshow the panel
                mInterviewPanel.setVisibility(View.VISIBLE);
            }
            else {
                // populate the questions view
                mInterviewPanel.removeAllViews();
                for (String q : mQuestions) {
                    View v = generateQuestionView(q);
                    mInterviewPanel.addView(v);
                }
                mInterviewPanel.setVisibility(View.VISIBLE);
                mQuestionsPopulated = true;
            }
        }
    }

    private void hideInterviewQuestions(){
        mInterviewPanel.setVisibility(View.GONE);
    }

    private View generateQuestionView(String question){
        View v = mInflater.inflate(R.layout.interview_question, null);
        ((TextView) v.findViewById(R.id.question_text)).setText(question);
        return v;
    }

    public void setQuestions(List<String> questions){
        mQuestions = questions;
    }
}
