package pl.edu.agh.to2.cleaner.logging;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.AppenderBase;
import ch.qos.logback.core.encoder.LayoutWrappingEncoder;
import ch.qos.logback.core.layout.EchoLayout;
import pl.edu.agh.to2.cleaner.gui.presenter.FileChoosePresenter;

public class GuiLogAppender extends AppenderBase<ILoggingEvent> {

    private static FileChoosePresenter presenter;

    private LayoutWrappingEncoder<ILoggingEvent> encoder;

    public static void setPresenter(FileChoosePresenter presenter) {
        GuiLogAppender.presenter = presenter;
    }

    public void setEncoder(LayoutWrappingEncoder<ILoggingEvent> encoder) {
        this.encoder = encoder;
    }

    @Override
    protected void append(ILoggingEvent eventObject) {
//        System.out.println("GuiLogAppender: Received log - " + eventObject.getFormattedMessage());
        if (presenter != null) {
            String formattedMessage = encoder != null
                    ? encoder.getLayout().doLayout(eventObject)
                    : eventObject.getFormattedMessage(); // Fallback to plain message
            presenter.addLog(formattedMessage.trim());
        }
    }
}



