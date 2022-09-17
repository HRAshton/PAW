/* Author: HRAshton */
const aDigits = "0123456789"
const aLowers = "abcdefghijklmnopqrstuvwxyz"
const aUppers = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
const aSpecs  = "_"

function countOf(haystack, needle) {
	var count = 0
	for (var i = 0; i < haystack.length; i++)
		if ($.inArray(haystack[i], needle) > -1)
			count++
	return count
}

function whatAAlphabet(chr) {
	if ($.inArray(chr, aDigits) > -1)
		return aDigits
	else
		if ($.inArray(chr, aLowers) > -1)
			return aLowers
		else
			if ($.inArray(chr, aUppers) > -1)
				return aUppers
			else
				return aSpecs
}

function strrep(string, chr, index) {
	begin = string.slice(0, index)
	end   = string.slice(index + 1)
	return begin + chr + end
}

function getPassword(site, keyphrase) {
	var password   = ""
	var fewChars  = []
	var pwdLength = 0
	var hashsum    = ""
	
	hashsum = $.md5(site + keyphrase)
	pwdLength = parseInt(hashsum.slice(29, 31), 16) % 2 + 11
	
	var i = 0
	while (i != 2 * pwdLength) {
		var num = parseInt(hashsum.slice(i, i + 2), 16)
		password += (aDigits + aLowers + aUppers + aSpecs)[num % 63]
		i += 2
	}
	
	var digits = countOf(password, aDigits)
    var lowers = countOf(password, aLowers)
    var uppers = countOf(password, aUppers)
    var specs  = countOf(password, aSpecs)
	
	if (digits < 2)
        fewChars[fewChars.length] = aDigits
    if (lowers < 2)
        fewChars[fewChars.length] = aLowers
    if (uppers < 2)
        fewChars[fewChars.length] = aUppers
    if (specs < 2)
        fewChars[fewChars.length] = aSpecs
	
	var j = parseInt(hashsum.slice(25, 27), 16) % 4
	for (i = 0; i < fewChars.length; i++) {
		alphabet = fewChars[i]
		while (countOf(password, alphabet) < 2) {
			q = whatAAlphabet(password[j % pwdLength])
			
			w = countOf(password, q)
            if (w > 2) {
                newChar = alphabet[parseInt(hashsum.slice(24, 26), 16) % alphabet.length]
                password = strrep(password, newChar, j % pwdLength)
			}
            j += 7
		}
	}
	
    return password
}










