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
 *
 * @vesion 1.0.0 - 2021/11/25_18:32:21<br>
 * @author Shinacho<br>
 */
public class FieldMapStorage extends Storage<FieldMap> implements XMLFileSupport {

	private static final FieldMapStorage INSTANCE = new FieldMapStorage();

	public static FieldMapStorage getInstance() {
		return INSTANCE;
	}

	private FieldMapStorage() {
	}

	@Override
	public void readFromXML(String filePath) throws IllegalXMLFormatException, FileNotFoundException, FileIOException {

		XMLFile file = new XMLFile(filePath);
		if (!file.getFile().exists()) {
			throw new FileNotFoundException(file + " is not found");
		}

		XMLElement root = file.load().getFirst();

		for (XMLElement e : root.getElement("fieldMap")) {
			String name = e.getAttributes().get("name").asId();
			XMLFile fieldMapDataFile = new XMLFile(e.getAttributes().get("data").asFileName());
			if (!fieldMapDataFile.exists()) {
				throw new IllegalXMLFormatException("data file is not found " + fieldMapDataFile);
			}
			add(new FieldMap(name, fieldMapDataFile));
		}
		if (GameSystem.isDebugMode()) {
			GameLog.print(getAll());
		}
//		printAll(System.out);
	}

}
