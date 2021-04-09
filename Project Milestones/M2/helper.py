import os

def replace(w1, w2, l):
    i = l.index(w1)
    l.pop(i)
    l.insert(i, w2)
    return l

def dothing(bruv):
    if (len(bruv) == 3):
        bruv[1], bruv[2] = bruv[2], bruv[1]
        bruv.insert(2, ":")
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
