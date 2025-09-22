/*
 * Copyright (C) 2025 Shinacho
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
package kinugasa.game.sample;

import java.time.LocalDate;
import java.time.temporal.Temporal;
import java.util.LinkedHashMap;

/**
 * Test3.<br>
 *
 * @vesion 1.0.0 - 2025/08/22_23:32:06<br>
 * @author Shinacho.<br>
 */
public class Test3 {

	public static String get星座(int m, int d) {
		LinkedHashMap<LocalDate, String> data = new LinkedHashMap<>();
		data.put(LocalDate.of(2025, 1, 20), "みずがめ座");
		data.put(LocalDate.of(2025, 2, 19), "うお座");
		data.put(LocalDate.of(2025, 3, 21), "おうし座");
		data.put(LocalDate.of(2025, 5, 21), "ふたご座");
		data.put(LocalDate.of(2025, 6, 22), "かに座");
		data.put(LocalDate.of(2025, 7, 23), "しし座");
		data.put(LocalDate.of(2025, 8, 23), "おとめ座");
		data.put(LocalDate.of(2025, 9, 23), "てんびん座");
		data.put(LocalDate.of(2025, 10, 24), "さそり座");
		data.put(LocalDate.of(2025, 11, 23), "いて座");
		data.put(LocalDate.of(2025, 12, 22), "やぎ座");

		LocalDate tgt = LocalDate.of(2025, m, d);
		var r = "みずがめ座";
		for (var v : data.entrySet()) {
			if (v.getKey().isAfter(tgt)) {
				return r;
			} else {
				r = v.getValue();
			}
		}
		return "やぎ座";

	}

	public static void main(String[] args) {
		System.out.println(get星座(12, 31));
	}
}
