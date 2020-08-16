package weka.core.progress;

import lombok.extern.log4j.Log4j2;

// TODO document
// TODO implement indeterminate bar

@Log4j2
public class TextProgressBar extends AbstractProgressBar {

    public TextProgressBar(double maxProgress, String progressMessage) {
        super(maxProgress, progressMessage);
    }

    private String progressChar = "=";

    private String progressRemainingChar = " ";

    protected int progressBarSize = 60;

    private int numDots = 0;

    @Override
    protected void onSetProgress() {
        numDots = (int) (m_normalizedProgress * progressBarSize);
    }

    @Override
    public void refreshDisplay() {
        System.err.print(String.format("\r%s: [%s%s]", m_progressMessage, progressChar.repeat(numDots), progressRemainingChar.repeat(progressBarSize - numDots)));
    }

    @Override
    public void show() {
        System.err.println("\n\n");
        refreshDisplay();
    }

    @Override
    public void finish() {
        System.err.println("\nDone!\n");
    }
}