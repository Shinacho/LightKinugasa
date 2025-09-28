/*
 * Copyright (C) 2023 Shinacho
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package kinugasa.field4;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import kinugasa.game.GameLog;
import kinugasa.system.UniversalValue;
import kinugasa.graphics.KImage;
import kinugasa.resource.ContentsIOException;
import kinugasa.resource.text.CSVFile;
import kinugasa.util.StringUtil;


/**
 *
 * @version 1.0.0 - 2022/11/14_8:56:58<br>
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
		reader.free();
		writer.save();
		GameLog.print("変換は正常に終了しました");
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
			GameLog.print(keyList.get(i));
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
