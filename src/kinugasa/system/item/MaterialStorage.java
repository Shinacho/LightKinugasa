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
package kinugasa.system.item;

import java.io.File;
import kinugasa.game.annotation.Singleton;
import kinugasa.resource.text.CSVFile;
import kinugasa.resource.text.TextFile;

/**
 * MaterialStorage.<br>
 *
 * @vesion 1.0.0 - 2025/10/20_0:10:25<br>
 * @author Shinacho.<br>
 */
@Singleton
public class MaterialStorage {

	private static final MaterialStorage INSTANCE = new MaterialStorage();

	private MaterialStorage() {
	}

	public static MaterialStorage getInstance() {
		return INSTANCE;
	}
	
	public void init(CSVFile f){
		for( var v : f.load()){
			//KEY,PRICE # name
			
		}
		f.free();
	}

}
