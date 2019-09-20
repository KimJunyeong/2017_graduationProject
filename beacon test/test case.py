from openpyxl import Workbook

w = 9
h = 10
array = [[0]*h for i in range(w)]

def mean(data):
    """Return the sample arithmetic mean of data."""
    n = len(data)
    if n < 1:
        raise ValueError('mean requires at least one data point')
    return sum(data)/n # in Python 2 use sum(data)/float(n)

def _ss(data):
    """Return sum of square deviations of sequence data."""
    c = mean(data)
    ss = sum((x-c)**2 for x in data)
    return ss

def stddev(data, ddof=0):
    """Calculates the population standard deviation
    by default; specify ddof=1 to compute the sample
    standard deviation."""
    n = len(data)
    if n < 2:
        raise ValueError('variance requires at least two data points')
    ss = _ss(data)
    pvar = ss/(n-ddof)
    return pvar**0.5

with open('file') as f:
    i = 0
    j = 0
    for line in f: # read rest of lines
        array[i][j] = int(line.split()[0])
        i+=1
        if i==9:
            i=0
            j+=1
    # print(array)

write_wb = Workbook()
write_ws = write_wb.active
write_ws['A1'] = '8 meter'

index = 1
for i in array:
    print(mean(i))
    print(stddev(i))
    print()
    write_ws.append([index, mean(i), stddev(i)])
    index+=1

write_wb.save('./test.xlsx')