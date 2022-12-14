import trainingRecordApi from '@/api/training-record'
import Sortable from 'sortablejs'

export default {
  name: 'TrainingRecordList',

  data() {
    return {
      total: 0, // 总记录数
      searchParam: {
        keyword: '', // 查询关键字
        current: 1, // 页码
        size: 10, // 每页记录数
        field: 'training_teacher', // 查询条件
        sortBy: 'gmt_modified', // 排序条件
        sortAsc: false // 正序排序
      },
      tableUpdateKey: 0,// 数据表表头调整后刷新
      tableColumns: [
        {
          title: '培训老师',
          key: 'trainingTeacher',
          sortable: 'custom',
          resizable: true,
          width: 180
        },
        {
          title: '培训日期',
          key: 'trainingDate',
          sortable: 'custom',
          resizable: true,
          width: 180,
          render: (h, params) => {
            // 设置时间格式
            const format = 'YY年MM月DD日'
            // 获取单元格数据
            if (params.row.trainingDate == null) {
              return null
            }
            const date = new Date(params.row.trainingDate)

            // 创建数组，如果数字小于 10，则在十位上填充 0
            const preArr = [
              '00',
              '01',
              '02',
              '03',
              '04',
              '05',
              '06',
              '07',
              '08',
              '09'
            ]

            const year = date.getFullYear()
            const month = date.getMonth() + 1 // 月份是从0开始的
            const day = date.getDate()

            const newTime = format
              .replace(/YY/g, year)
              .replace(/MM/g, preArr[month] || month)
              .replace(/DD/g, preArr[day] || day)
            return h('Tag', {}, newTime)
          }
        },
        {
          title: '培训内容',
          key: 'trainingContent',
          resizable: true,
          width: 480
        },
        {
          key: 'option',
          title: '操作',
          slot: 'action',
          resizable: true,
          width: 150,
          align: 'center'
        }
      ],
      tableData: [] // 表格数据
    }
  },
  created() {
    // 当页面加载时获取数据
    this.getTableData()
  },
  mounted() {

    let columnAttribute = this.$store.state.trainingRecordList.columnAttribute
    if (columnAttribute.length !== 0) {
      let newTableColumns = []

      this.tableColumns.forEach((item) => {
        const t = columnAttribute.find((i) => (i.key === item.key))
        item.width = t.width
        newTableColumns[t.situation] = item
      })

      this.tableColumns = newTableColumns
    }
    this.columnDrop()

  },
  methods: {
    getTableData() {
      trainingRecordApi
        .trainingRecordComplexQuery(this.searchParam)
        .then((response) => {
          // debugger 设置断点调试
          if (response.success === true) {
            this.tableData = response.data.items
            this.total = response.data.total
            this.tableData.forEach((item) => (item.children = item.list))
          }
        })
    },
    //列拖拽
    columnDrop() {
      const wrapperTr =
        this.$refs.mainTable.$refs.header.firstChild.children[1].children[0]

      this.sortable = Sortable.create(wrapperTr, {
        animation: 180,
        delay: 0,
        onEnd: (evt) => {
          const oldItem = this.tableColumns[evt.oldIndex]
          this.tableColumns.splice(evt.oldIndex, 1)
          this.tableColumns.splice(evt.newIndex, 0, oldItem)
          this.tableUpdateKey += 1

          this.saveTableState()
          this.$nextTick(function() {
            this.columnDrop()
          })
        }
      })
    },
    changePage(page) {
      this.searchParam.current = page
      this.getTableData()
    },
    clearKeyword() {
      this.searchParam.keyword = ''
      this.getTableData()
    },
    search(keyword) {
      this.searchParam.keyword = keyword
      this.getTableData()
    },
    reSortTableData(column) {
      switch (column.key) {
        case 'name':
          this.searchParam.sortBy = 'name'
          break
        case 'department':
          this.searchParam.sortBy = 'department'
          break
        default:
          this.searchParam.sortBy = 'gmt_modified'
      }
      this.searchParam.sortAsc = column.order == 'asc'
      this.getTableData()
    },
    edit(row) {
      this.$router.push({ name: 'TrainingRecordEdit', params: { id: row.id } })
    },
    show(row) {
      this.$router.push({ name: 'TrainingRecordShow', params: { id: row.id } })
    },
    remove(row) {
      let dataform = { id: row.id }
      trainingRecordApi.delete(dataform).then((response) => {
        if (response.success === true) {
          this.getTableData()
        }
      })
    },
    saveTableState() {
      let columnAttribute = this.tableColumns.map((obj, index) => {
        let t = {}
        t.situation = index
        t.key = obj.key
        t.width = obj.width
        return t
      })
      this.$store.commit(
        'trainingRecordList/SET_COLUMNATTRIBUTE',
        columnAttribute
      )
    }
  }
}
