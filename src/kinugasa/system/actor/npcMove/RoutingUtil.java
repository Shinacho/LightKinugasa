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
package kinugasa.system.actor.npcMove;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;
import kinugasa.field4.D2Idx;
import kinugasa.field4.FieldMap;
import kinugasa.system.actor.CharaSprite;

/**
 * RoutingUtil.<br>
 *
 * @vesion 1.0.0 - 2025/09/23_23:48:39<br>
 * @author Shinacho.<br>
 */
public class RoutingUtil {

	private RoutingUtil() {
	}

	public static List<D2Idx> getRoute(FieldMap fm, CharaSprite sprite, final D2Idx start, final D2Idx goal) {
		int W = fm.getNomalLayerSprite().get(0).getDataWidth();
		int H = fm.getNomalLayerSprite().get(0).getDataHeight();
		//コスト表
		int[][] cost = new int[H][W];
		for (int y = 0; y < H; y++) {
			for (int x = 0; x < W; x++) {
				D2Idx i = new D2Idx(x, y);
				cost[y][x] = canStep(fm, sprite, i) ? 1 : Integer.MAX_VALUE;
			}
		}
		final List<D2Idx> DIR8 = List.of(new D2Idx(-1, -1), new D2Idx(-1, 0), new D2Idx(-1, 1), new D2Idx(0, -1), new D2Idx(0, 1), new D2Idx(1, -1), new D2Idx(1, 0), new D2Idx(1, 1));
		final int STEP = 10;
		int[][] g = new int[H][W];
		for (int[] row : g) {
			Arrays.fill(row, Integer.MAX_VALUE);
		}
		int[][] py = new int[H][W];
		for (int[] row : py) {
			Arrays.fill(row, -1);
		}
		int[][] px = new int[H][W];
		for (int[] row : px) {
			Arrays.fill(row, -1);
		}

		class Node {

			final D2Idx idx;
			final int f;
			final int g;

			public Node(D2Idx idx, int f, int g) {
				this.idx = idx;
				this.f = f;
				this.g = g;
			}
		}
		PriorityQueue<Node> open = new PriorityQueue<>(Comparator.comparing(n -> n.f));
		g[start.y][start.x] = 0;
		open.add(new Node(start.clone(), heuristic(start, goal), 0));
		boolean[][] closed = new boolean[H][W];
		while (!open.isEmpty()) {
			Node c = open.poll();
			if (closed[c.idx.y][c.idx.x]) {
				continue;
			}
			closed[c.idx.y][c.idx.x] = true;
			if (c.idx.y == goal.y && c.idx.x == goal.x) {
				return reconstruct(py, px, goal);
			}
			for (D2Idx d : DIR8) {
				D2Idx n = c.idx.add(d);
				if (!canStep(fm, sprite, n)) {
					continue;
				}

				/*
				コーナーカット禁止
				d = 0 && ( ! canStep(c.x + d.x, c.y ) || ! canStep(c.x, c.y + d.y)) continue 
				 */
				int ng = g[c.idx.y][c.idx.x] + STEP;
				if (ng < g[n.y][n.x]) {
					g[n.y][n.x] = ng;
					py[n.y][n.x] = c.idx.y;
					px[n.y][n.x] = c.idx.x;
					int f = ng + heuristic(n, goal);
					open.add(new Node(n, f, ng));
				}
			}
		}
		return List.of();
	}

	public static boolean canStep(FieldMap fm, CharaSprite sprite, D2Idx i) {
		return fm.getTile(new D2Idx(i.x, i.y)).canStep(sprite.getVehicle());
	}

	private static int heuristic(D2Idx start, D2Idx goal) {
		int dy = Math.abs(start.y - goal.y);
		int dx = Math.abs(start.x - goal.x);
		return 10 * (Math.max(dy, dx));
	}

	private static List<D2Idx> reconstruct(int[][] py, int[][] px, D2Idx goal) {
		List<D2Idx> r = new ArrayList<>();
		D2Idx c = goal.clone();
		while (c.y != -1 && c.y != -1) {
			r.add(c.clone());
			c = new D2Idx(px[c.y][c.x], py[c.y][c.x]);
		}
		Collections.reverse(r);
		return r;
	}

}
