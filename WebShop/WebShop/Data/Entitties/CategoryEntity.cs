using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;

namespace WebShop.Data.Entitties
{
    [Table("tblCategories")]
    public class CategoryEntity : BaseEntity<int>
    {
        [Required, StringLength(255)]
        public string Name { get; set; }
        [StringLength(255)]
        public string Image { get; set; }
        public int Priority { get; set; }
        [StringLength(4000)]
        public string Description { get; set; }
    }
}
