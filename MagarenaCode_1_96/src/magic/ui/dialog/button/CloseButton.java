package magic.ui.dialog.button;

import magic.data.MagicIcon;
import magic.ui.MagicImages;
import magic.translate.MText;

@SuppressWarnings("serial")
public class CloseButton extends MagicDialogButton {

    // translatable strings
    private static final String _S1 = "Close";

    public CloseButton() {
        super(MText.get(_S1));
        setIcon(MagicImages.getIcon(MagicIcon.BANNED));
    }

}
