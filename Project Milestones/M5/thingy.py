import os
import re
# barely functional UML parser
# doesnt tokenize <> or () properly
#     my big brain decided spaces were enough to tokenize

def replace(w1, w2, l):
    i = l.index(w1)
    l.pop(i)
    l.insert(i, w2)
    return l

def dothing(bruv):
    if len(bruv) == 2:
        print(" ".join(bruv) + ":") # class declarator? hopefully
        return
    if (len(bruv) > 3): # hopefully has parathesis
        m = re.search("\([A-z ,]*\)", " ".join(bruv))
        if m:
            m = m.group().strip("()")
            m = m.split(",")
            nm = []
            for arg in m:
                arg = [k for k in arg.split(" ") if k]
                arg.reverse()
                arg = ": ".join(arg)
                nm += [arg]
            m = "(" + ", ".join(nm) + ")"
            
            full = (re.sub("\([A-z ,]*\)", m,  " ".join(bruv)))
            bruv = full.split(" ")
    rtype = bruv.pop(1)
    bruv += [": " + rtype]
    print(" ".join(bruv))

for filename in os.listdir(os.getcwd()):
    print()
    print(filename)
    with open(filename, 'r') as f:
        for line in f.readlines():
            line = line.strip()
            words = [i.strip(";") for i in line.split(" ") if i != "{" and i != "final"]
            if "public" in words:
                bruv = replace("public", "+", words)
                dothing(bruv)
            if "private" in words:
                bruv = replace("private", "-", words)
                dothing(bruv)
            if "protected" in words:
                bruc = replace("protected", "*")
                dothing(bruv)
