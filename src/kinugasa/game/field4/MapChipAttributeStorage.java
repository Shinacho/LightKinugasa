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

import kinugasa.game.GameLog;
import kinugasa.game.system.GameSystem;
import kinugasa.resource.FileIOException;
import kinugasa.resource.Storage;
import kinugasa.resource.FileNotFoundException;
import kinugasa.resource.text.IllegalXMLFormatException;
import kinugasa.resource.text.XMLElement;
import kinugasa.resource.text.XMLFile;
import kinugasa.resource.text.XMLFileSupport;

/**
 * マップチップの属性の唯一の保管庫です。このクラスはシングルトンです。
 *
 * @vesion 1.0.0 - 2022/11/08_16:12:11<br>
 * @author Shinacho<br>
 */
public class MapChipAttributeStorage extends Storage<MapChipAttribute> implements XMLFileSupport {

	private static final MapChipAttributeStorage INSTANCE = new MapChipAttributeStorage();

	public static MapChipAttributeStorage getInstance() {
		return INSTANCE;
	}
	public static final MapChipAttribute VOID = new MapChipAttribute("VOID");
	public static final MapChipAttribute CLOSE = new MapChipAttribute("CLOSE");

	static {
		getInstance().add(VOID);
		getInstance().add(CLOSE);
	}

	@Override
	public void readFromXML(String filePath) throws IllegalXMLFormatException, FileNotFoundException, FileIOException {
		XMLFile file = new XMLFile(filePath);
		if (!file.getFile().exists()) {
			throw new FileNotFoundException(file + " is not found");
		}

		XMLElement root = file.load().getFirst();
		for (XMLElement e : root.getElement("attribute")) {
			String name = e.getAttributes().get("name").asId();
			int encountBaseValue = e.getAttributes().contains("encountBaseValue") ? e.getAttributes().get("encountBaseValue").asInt() : 0;
			getInstance().add(new MapChipAttribute(name, encountBaseValue));
		}
		if (GameSystem.isDebugMode()) {
			GameLog.print(getAll());
		}

	}

}
