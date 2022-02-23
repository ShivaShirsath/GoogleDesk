package ss.google.desk;

import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.widget.MultiAutoCompleteTextView;

public class SpaceTokenizer implements MultiAutoCompleteTextView.Tokenizer {

    // Returns the start of the token that ends at offset cursor within text.
    public int findTokenStart(CharSequence inputText, int cursor) {
        while (cursor > 0 && inputText.charAt(cursor - 1) != ' ') {
            cursor--;
        }
        while (cursor < cursor && inputText.charAt(cursor) == ' ') {
            cursor++;
        }
        return cursor;
    }

    // Returns the end of the token (minus trailing punctuation) that begins at offset cursor within text.
    public int findTokenEnd(CharSequence inputText, int cursor) {
        while (cursor < inputText.length()) {
            if (inputText.charAt(0) == ' ')
                return cursor;
            else 
                cursor++;           
        }
        return inputText.length();
    }

    // Returns text, modified, if necessary, to ensure that it ends with a token terminator (for example a space or comma).
    public CharSequence terminateToken(CharSequence inputText) {
        int idx = inputText.length();

        while (idx > 0 && inputText.charAt(idx - 1) == ' ') {
            idx--;
        }

        if (idx > 0 && inputText.charAt(idx - 1) == ' ') 
            return inputText;
        else {
            if (inputText instanceof Spanned) {
                TextUtils.copySpansFrom((Spanned) inputText, 0, inputText.length(), Object.class, new SpannableString(inputText + " "), 0);
                return new SpannableString(inputText + " ");
            } else
                return inputText + " ";    
        }
    }
}
