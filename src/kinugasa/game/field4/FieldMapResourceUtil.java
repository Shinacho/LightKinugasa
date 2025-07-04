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

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import kinugasa.game.system.UniversalValue;
import kinugasa.resource.*;
import kinugasa.resource.text.*;
import kinugasa.graphics.*;
import kinugasa.util.StringUtil;

/**
 *
 * @vesion 1.0.0 - 2022/11/14_8:56:58<br>
 * @author Shinacho<br>
 */
public class FieldMapResourceUtil {

	private FieldMapResourceUtil() {
	}

	/**
	 * .
	 * <br>
	 * Type1かつ、パーツが16bitである必要があります。<br>
	 * <br>
	 *
	 * @param input
	 * @param output
	 * @param w イメージのタイル数。
	 * @param h イメージのタイル数。
	 * @throws ContentsIOException
	 */
	public static void platinumCsvType1ToKGCsv(File input, File output, int w, int h)
			throws ContentsIOException {
		if (!input.exists() | output.exists()) {
			throw new ContentsIOException("File is Already Exists");
		}

		CSVFile reader = new CSVFile(input);
		CSVFile writer = new CSVFile(output);

		String[] convertTable = new String[65536];
		for (int i = 0, k = 0; i < h; i++) {
			for (int j = 0; j < w; j++) {
				String y = Integer.toString(i);
				while (y.length() != 3) {
					y = "0" + y;
				}
				String x = Integer.toString(j);
				while (x.length() != 3) {
					x = "0" + x;
				}
				convertTable[k++] = y + x;
			}
		}
		List<List<UniversalValue>> inputData = reader.load().getData();
		for (var inputLine : inputData) {
			if (inputLine.isEmpty()) {
				continue;
			}
			if (inputLine.get(0).value().equals("")) {
				writer.add(new String[]{"-"});
				continue;
			}
			String[] outputLine = new String[inputLine.size()];
			for (int j = 0; j < inputLine.size(); j++) {
				outputLine[j] = convertTable[Integer.parseInt(inputLine.get(j).value())];
			}
			//末尾の/
			outputLine[outputLine.length - 1] += "/";
			writer.add(outputLine);
		}
		reader.dispose();
		writer.save();
		kinugasa.game.GameLog.print("変換は正常に終了しました");
	}

	/**
	 * splitAsMapN(3桁)で切り出せる画像のIDのセットを出力します。 これはチップセットXMLを構築する際に有用です。<br>
	 *
	 * @param filePath チップセット画像のパス<br>
	 */
	public static void printImageId(String filePath, int w, int h) {
		KImage image = new KImage(filePath);

		Map<String, KImage> map = image.splitAsMap(w, h, (y, x) -> StringUtil.zeroUme(y, 3) + StringUtil.zeroUme(x, 3));
		List<String> keyList = new ArrayList<>(map.keySet());
		Collections.sort(keyList);
		for (int i = 0, size = keyList.size(); i < size; i++) {
			kinugasa.game.GameLog.print(keyList.get(i));
		}
	}

	//outer16
	public static void main(String[] args) {

		String f1 = "D:/Project/FuzzyWorld/resource/data/map/raw/map05.csv";
		String f2 = "D:/Project/FuzzyWorld/resource/data/map/converted/map05.csv";
		platinumCsvType1ToKGCsv(new File(f1), new File(f2), 16, 48);//outer
//		platinumCsvType1ToKGCsv(new File(f1), new File(f2), 8, 16);//inner16_2

	}
}
