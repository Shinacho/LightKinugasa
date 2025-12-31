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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * XText.<br>
 *
 * @vesion 1.0.0 - 2025/12/24_19:52:36<br>
 * @author Shinacho.<br>
 */
public class XText {

	private String text;
	private XHashTagLocation location;
	private List<XHashTag> tags;

	public XText(String text) {
		this.text = text;
	}

	public XText(String text, XHashTagLocation l, List<String> tags) {
		this.text = text;
		this.location = l;
		this.tags = tags.stream().map(p -> new XHashTag(p)).toList();
	}

	public XText(String text, XHashTagLocation l, String... tags) {
		this(text, l, Arrays.asList(tags));
	}

	public XText trimText() {
		int max = 280;
		for (var v : tags) {
			max -= v.lengrh() + 1;
		}
		max -= tags.size();//" "

		int l = 0;
		int s = 0;
		for (char c : text.toCharArray()) {
			l += XUtil.is半角(c) ? 1 : 2;
			s++;
			if (l >= max) {
				break;
			}
		}
		text = text.substring(s);
		XText r = new XText(text);
		r.location = this.location;
		r.tags = new ArrayList<>(this.tags);
		return r;
	}

	public String getText() {
		return text;
	}

	public XHashTagLocation getLocation() {
		return location;
	}

	public List<XHashTag> getTags() {
		return tags;
	}

	@Override
	public String toString() {
		StringBuilder s = new StringBuilder();
		if (tags != null && location != null && !tags.isEmpty()) {
			if (location == XHashTagLocation.TOP) {
				s.append(String.join(" ", tags.stream().map(p -> p.toString()).toList()));
				s.append(' ');
				s.append(text);

			} else {
				s.append(text);
				s.append(' ');
				s.append(String.join(" ", tags.stream().map(p -> p.toString()).toList()));
			}
		}
		return s.toString();
	}

}
