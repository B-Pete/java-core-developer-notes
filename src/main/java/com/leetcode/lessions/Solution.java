package com.leetcode.lessions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Solution {

	public static void main(String[] args) {
		Solution s = new Solution();
		System.out.println(s.findSubstring(
				"aaaaaaaaaaaaaa", new String[] { "aa", "aa" }));
		System.out.println(s.findSubstring(
				"wordgoodgoodgoodbestword",	new String[]{"word","good","best","good"}));
		System.out.println(s.findSubstring(
				"barfoobaarbaarfooothebaarfooobaarman",	new String[]{"fooo","baar", "baar"}));
		System.out.println(s.findSubstring(
				"foobarfoobar",	new String[]{"foo","bar"}));
		System.out.println(s.findSubstring(
				"barfoothefoobarman",	new String[]{"foo","bar"}));
		System.out.println(s.findSubstring(
				"barfoofoobarthefoobarman",	new String[]{"bar","foo","the"}));
	}

	public List<Integer> findSubstring(String s, String[] words) {
		Map<String, Integer> m = new HashMap<>();
		for (String word: words) {
			// map value contains number of each word occurrences.
			m.put(word, m.getOrDefault(word, 0) + 1);
		}
		int wordLen = words[0].length();
		int concatinatedLen = wordLen*words.length;

		List<Integer> out = new ArrayList<>();
		int limit  = s.length() - concatinatedLen;
		// String is concatenated by words which have same length.
		// For each character in a word, do check by letting pointer jumps each step a word length.
		for (int i=0; i<wordLen; i++){
			int startIdx = i;
			while (startIdx <= limit) {
				// copy start index
				int start = startIdx;
				// begin loop that check substring from 'end' to 'start'.
				int end = start+concatinatedLen;
				// matched words count
				int count = 0;
				// clone the map to retain words occurrences each check loop
				Map<String, Integer> map = new HashMap<>(m);
				while (start < end) {
					String lastWord = s.substring(end-wordLen, end);
					if (map.containsKey(lastWord) && map.get(lastWord) > 0) {
						count++;
						// matched word found, reduce number of word occurrences.
						map.put(lastWord, map.get(lastWord) - 1);

						if (count == words.length) {
							out.add(startIdx);
							// all words matched, move pointer to next word index.
							start = startIdx+wordLen;
							break;
						}
						// last matched found at 'end' (*)
						// now move pointer back 1 word and continue check until first word.
						end -= wordLen;
					} else {
						// no word found from 'end' to 'start', move pointer directly to last matched (*).
						start = end;
						break;
					}
				}
				// reset start index
				startIdx = start;

			}
		}
		return out;
	}
}
