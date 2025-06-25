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

import kinugasa.game.system.NPCSprite;
import java.awt.Point;
import kinugasa.object.IDdCloneableObject;
import kinugasa.object.ID;

/**
 *
 * @vesion 1.0.0 - 2022/11/08_19:19:27<br>
 * @author Shinacho<br>
 */
public abstract class NPCMoveModel extends IDdCloneableObject {

	public NPCMoveModel(String id) {
		super(id);
	}

	public abstract D2Idx getNextTargetIdx(NPCSprite n, FieldMap map);

	public abstract int nextMoveFrameTime(NPCSprite n, FieldMap map);

	public abstract D2Idx getMin(NPCSprite n);

	public abstract D2Idx getMax(NPCSprite n);

	@Override
	public NPCMoveModel clone() {
		return (NPCMoveModel) super.clone();
	}

}
