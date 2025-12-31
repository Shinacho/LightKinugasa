/*
 * The MIT License
 *
 * Copyright 2025 owner.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package kinugasa.xshare;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * XShare.<br>
 *
 * @vesion 1.0.0 - 2025/12/24_19:50:15<br>
 * @author Shinacho.<br>
 */
public class XShare {

	private static final XShare INSTANCE = new XShare();

	private XShare() {
	}

	public static XShare getInstance() {
		return INSTANCE;
	}

	private static final String URL = "https://twitter.com/intent/tweet?text=";

	public boolean open(XText text) {
		try {
			String url = URL + URLEncoder.encode(text.toString(), StandardCharsets.UTF_8);
			Desktop.getDesktop().browse(new URI(url));
		} catch (IOException ex) {
			ex.printStackTrace();
			return false;
		} catch (URISyntaxException ex) {
			ex.printStackTrace();
			return false;
		}
		return true;
	}
}
