 /*
  * MIT License
  *
  * Copyright (c) 2025 しなちょ
  *
  * Permission is hereby granted, free of charge, to any person obtaining a copy
  * of this software and associated documentation files (the "Software"), to deal
  * in the Software without restriction, including without limitation the rights
  * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
  * copies of the Software, and to permit persons to whom the Software is
  * furnished to do so, subject to the following conditions:
  *
  * The above copyright notice and this permission notice shall be included in all
  * copies or substantial portions of the Software.
  *
  * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
  * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
  * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
  * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
  * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
  * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
  * SOFTWARE.
  */


package kinugasa.game.ui;

import kinugasa.resource.NotFoundException;


/**
 * テキストセットで定義されたNextIDが同じテキストセット内に存在しない場合の例外です.
 * <br>
 *
 * @version 1.0.0 - 2015/03/21<br>
 * @author Shinacho<br>
 * <br>
 */
public class TextIdNotFoundException extends NotFoundException {

    /**
     * Creates a new instance of <code>TextIdNotFoundException</code> without detail message.
     */
    public TextIdNotFoundException() {
    }


    /**
     * Constructs an instance of <code>TextIdNotFoundException</code> with the specified detail message.
     * @param msg the detail message.
     */
    public TextIdNotFoundException(String msg) {
        super(msg);
    }
}
