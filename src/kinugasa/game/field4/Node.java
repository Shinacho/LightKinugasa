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


package kinugasa.game.field4;

import kinugasa.game.I18N;
import kinugasa.resource.sound.Sound;
import kinugasa.object.FourDirection;
import kinugasa.object.ID;

/**
 *
 * @vesion 1.0.0 - 2021/11/25_18:35:10<br>
 * @author Shinacho<br>
 */
public class Node implements ID {

	private Mode mode;
	private String id;
	private int x, y;
	private String exitFieldMapName;
	private String exitNodeName;
	private String tooltip;
	private Sound se = null;
	private NodeAccepter accepter;
	private FourDirection outDir;

	public enum Mode {
		INOUT,
		OUT,
	}

	private Node() {
	}

	public static Node ofInOutNode(String id, String exitFieldMapName,
			String exitNodeName, int x, int y, String tooltip, NodeAccepter accepter, FourDirection outDir) {
		Node n = new Node();
		n.id = id;
		n.exitFieldMapName = exitFieldMapName;
		n.exitNodeName = exitNodeName;
		n.x = x;
		n.y = y;
		n.tooltip = tooltip;
		n.accepter = accepter;
		n.mode = Mode.INOUT;
		n.outDir = outDir;
		return n;
	}

	public static Node ofOutNode(String id, String outMapName, int x, int y, FourDirection outDir) {
		Node n = ofInOutNode(id, outMapName, null, x, y, null, null, null);
		n.mode = Mode.OUT;
		n.outDir = outDir;
		return n;
	}

	public Mode getMode() {
		return mode;
	}

	@Override
	public String getId() {
		return id;
	}

	public String getExitNodeName() {
		return exitNodeName;
	}

	public String getExitFieldMapName() {
		return exitFieldMapName;
	}

	public void setSe(Sound se) {
		this.se = se;
	}

	public Sound getSe() {
		return se;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public D2Idx getIdx() {
		return new D2Idx(x, y);
	}

	public String getTooltip() {
		return tooltip;
	}

	public String getTooltipI18Nd() {
		return I18N.get(tooltip);
	}

	public NodeAccepter getAccepter() {
		return accepter;
	}

	public FourDirection getOutDir() {
		return outDir;
	}

	@Override
	public String toString() {
		return "FieldMapNode{" + "mode=" + mode + ", name=" + id + ", x=" + x + ", y=" + y + ", exitFieldMapName=" + exitFieldMapName + ", exitNodeName=" + exitNodeName + ", tooltip=" + tooltip + ", se=" + se + ", accepter=" + accepter + '}';
	}

}
