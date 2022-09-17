from hashlib import md5 as md5summer
import re
import sys
import codecs

#
def get_pwd(site, keyphrase):
    def md5(str_):
        hash = md5summer()
        hash.update(str_)
        return str(hash.hexdigest())
    
    def count_of(haystack, needle): #Есть ли в haystack символы из needle?
        count = 0
        for a in haystack:
            if a in list(needle):
                count += 1
        return count
    
    def strrep(string, char, index):
        begin = string[:index]
        end = string[index + 1:]
        return begin + char + end
    
    def what_a_alphabet(char):
        if char in a_digits:
            return a_digits
        else:
            if char in a_lowers:
                return a_lowers
            else:
                if char in a_uppers:
                    return a_uppers
                else:
                    return a_specs
            
    a_digits  = '0123456789'
    a_lowers  = 'abcdefghigklmnopqrstuvwxyz'
    a_uppers  = 'ABCDEFGHIJKLMNOPQRSTUVWXYZ'
    a_specs   = '_'
    keyphrase = keyphrase
    site      = site.lower()
    password  = ''
    few_chars = []
    length    = 0


    site = re.sub(r"(.*?\.){0,}(.+)\..+", r"\2", site)


    hashsum = md5(site + keyphrase)
    length = int(hashsum[29:31], 16) % 2 + 11
    
    i = 0
    while i != 2 * length:
        num = int(hashsum[i : i + 2], 16)
        password += (a_digits + a_lowers + a_uppers + a_specs)[num % 63]
        i += 2
    print(password, int(hashsum[25:27], 16) % 4)
    digits = count_of(password, a_digits)
    lowers = count_of(password, a_lowers)
    uppers = count_of(password, a_uppers)
    specs  = count_of(password, a_specs)
    
    if (digits < 2):
        few_chars.append(a_digits)
    if (lowers < 2):
        few_chars.append(a_lowers)
    if (uppers < 2):
        few_chars.append(a_uppers)
    if (specs < 2):
        few_chars.append(a_specs)
    
    i = int(hashsum[25:27], 16) % 4
    for alphabet in few_chars:
        while (count_of(password, alphabet) < 2):
            if count_of(password, what_a_alphabet(password[i % length])) > 2:
                new_char = alphabet[int(hashsum[24:26], 16) % len(alphabet)]
                password = strrep(password, new_char, i % length)
            i += 7
    return password

#=============================================================================

a = str(raw_input()).decode("UTF-8").lower().encode("UTF-8")
b = str(raw_input())
pwd = get_pwd(a, b)
print pwd

