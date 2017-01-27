remote = False
psolt = "+jpk"

if remote:
    localdb = {
        'user': 'habashy',
        'password': 'scriptingninja',
        'host': 'localhost',
        'database': 'package'
    }
else:
    localdb = {
        'user': 'root',
        'password': '',
        'host': 'localhost',
        'database': 'package'
    }
