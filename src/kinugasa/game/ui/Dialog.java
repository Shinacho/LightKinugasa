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

import java.awt.Toolkit;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JTextField;
import kinugasa.game.I18NText;

/**
 *
 * @vesion 1.0.0 - 2021/08/17_6:52:34<br>
 * @author Shinacho<br>
 */
public class Dialog {

	public static DialogOption info(String title, String msg) {
		int r = JOptionPane.showConfirmDialog(null, msg, title, JOptionPane.DEFAULT_OPTION);
		return DialogOption.of(r);
	}

	public static DialogOption error(String title, String msg) {
		int r = JOptionPane.showConfirmDialog(null, msg, title, JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE);
		return DialogOption.of(r);
	}

	public static class InputResult {

		public InputResult(String value, DialogOption result) {
			this.value = value;
			this.result = result;
		}

		public String value;
		public DialogOption result;
	}

	public static InputResult input(String title, int max) {
		JTextField jt = new JTextField();
		jt.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				JTextField f = (JTextField) e.getSource();
				String s = f.getText();
				if (s.length() > max) {
					f.setText(s.substring(0, max));
				}
			}
		});
		jt.requestFocus();
		int r = JOptionPane.showConfirmDialog(null, jt, title, JOptionPane.DEFAULT_OPTION);
		return new InputResult(jt.getText(), DialogOption.of(r));
	}

	public static InputResult input(I18NText title, int max) {
		JTextField jt = new JTextField();
		jt.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				JTextField f = (JTextField) e.getSource();
				String s = f.getText();
				if (s.length() > max) {
					f.setText(s.substring(0, max));
				}
			}
		});
		jt.requestFocus();
		int r = JOptionPane.showConfirmDialog(null, jt, title.toString(), JOptionPane.DEFAULT_OPTION);
		return new InputResult(jt.getText(), DialogOption.of(r));
	}

	public static DialogOption yesOrNo(String title, DialogIcon icon, String msg) {
		int r = JOptionPane.showConfirmDialog(null, msg, title, JOptionPane.YES_NO_OPTION, icon.getOption());
		return DialogOption.of(r);
	}

	public static DialogOption yesOrNo(I18NText title, DialogIcon icon, I18NText msg) {
		int r = JOptionPane.showConfirmDialog(null, msg, title.toString(), JOptionPane.YES_NO_OPTION, icon.getOption());
		return DialogOption.of(r);
	}

	public static DialogOption okOrCancel(String title, DialogIcon icon, Object msg) {
		int r = JOptionPane.showConfirmDialog(null, msg, title, JOptionPane.DEFAULT_OPTION, icon.getOption());
		return DialogOption.of(r);
	}

	public static void image(String title, BufferedImage image) {
		JOptionPane.showMessageDialog(null, new ImageIcon(image), title, JOptionPane.DEFAULT_OPTION);
	}

	public static void image(I18NText title, BufferedImage image) {
		JOptionPane.showMessageDialog(null, new ImageIcon(image), title.toString(), JOptionPane.DEFAULT_OPTION);
	}

	public static DialogOption okOrCancel(I18NText title, DialogIcon icon, Object msg) {
		int r = JOptionPane.showConfirmDialog(null, msg, title.toString(), JOptionPane.DEFAULT_OPTION, icon.getOption());
		return DialogOption.of(r);
	}

	public static JDialog progressBar(I18NText title, String msg, JProgressBar bar) {
		JDialog d = new JDialog();
		d.setTitle(title.toString());
		JPanel p = new JPanel();
		p.add(new JLabel(msg));
		p.add(bar);
		d.add(p);
		d.setSize(300, 100);
		d.setResizable(false);
		int x = Toolkit.getDefaultToolkit().getScreenSize().width / 2 - 150;
		int y = Toolkit.getDefaultToolkit().getScreenSize().height / 2 - 50;
		d.setLocation(x, y);
		d.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		d.setModal(true);
		d.setModalExclusionType(java.awt.Dialog.ModalExclusionType.APPLICATION_EXCLUDE);
		d.pack();
		return d;
	}

	public static JDialog progressBar(String title, String msg, JProgressBar bar) {
		JDialog d = new JDialog();
		d.setTitle(title);
		JPanel p = new JPanel();
		p.add(new JLabel(msg));
		p.add(bar);
		d.add(p);
		d.setSize(300, 100);
		d.setResizable(false);
		int x = Toolkit.getDefaultToolkit().getScreenSize().width / 2 - 150;
		int y = Toolkit.getDefaultToolkit().getScreenSize().height / 2 - 50;
		d.setLocation(x, y);
		d.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		d.setModal(true);
		d.setModalExclusionType(java.awt.Dialog.ModalExclusionType.APPLICATION_EXCLUDE);
		d.pack();
		return d;
	}
}
